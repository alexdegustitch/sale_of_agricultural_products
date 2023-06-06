/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Cart;
import beans.Comment;
import beans.Company;
import beans.Farmer;
import beans.Item;
import beans.ItemCart;
import beans.Mark;
import beans.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.CartModel;
import models.CommentModel;
import models.CompanyModel;
import models.ItemCartModel;
import models.ItemModel;
import models.ItemOrderModel;
import models.MarkModel;
import models.OrderModel;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class ShopController {

    private String sort = "";
    private List<Item> items;
    private Integer mark_int = 1;
    private String type = null;

    private int item_id;

    private Item current_item;
    private Mark current_mark;
    private int mark;
    private int quantity_order = 1;

    private boolean ordered = false;

    //sluzi mi za komentare
    private int old_id_item;
    private int new_id_item;

    private List<Comment> comments;
    private Comment user_comment;

    private String comment_text = "";

     public void check_type() {
        HttpSession session = util.SessionUtils.getSession();
        String type = (String) session.getAttribute("type");

        FacesContext context = FacesContext.getCurrentInstance();
        if (type == null) {
            try {
                context.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("c")) {

            try {
                context.getExternalContext().redirect("company.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("a")) {
            try {
                context.getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public void get_all_items_for_company(String company_name) {
        if (type == null) {
            items = ItemModel.get_all_active_items_for_company(company_name);
        } else if (type.equals("A")) {
            items = ItemModel.get_all_active_agents_for_company(company_name);
        } else if (type.equals("S")) {
            items = ItemModel.get_all_active_seedlings_for_company(company_name);
        }

        for (int i = 0; i < items.size(); i++) {
            Company c = CompanyModel.find_company_by_short_name(items.get(i).getCompany_name());
            items.get(i).setCompany_full_name(c.getFull_name());
            items.get(i).setPrice(round(items.get(i).getPrice(), 2));
            items.get(i).setMark(round(items.get(i).getMark(), 2));
        }
        reorder_items();
    }

    public void get_all_items() {
        if (type == null) {
            items = ItemModel.get_all_active_items();
        } else if (type.equals("A")) {
            items = ItemModel.get_all_active_agents();
        } else if (type.equals("S")) {
            items = ItemModel.get_all_active_seedlings();
        }

        for (int i = 0; i < items.size(); i++) {
            Company c = CompanyModel.find_company_by_short_name(items.get(i).getCompany_name());
            items.get(i).setCompany_full_name(c.getFull_name());
            items.get(i).setPrice(round(items.get(i).getPrice(), 2));
            items.get(i).setMark(round(items.get(i).getMark(), 2));
        }
        reorder_items();
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void reorder_items() {

        if (sort == null) {
            return;
        }

        switch (sort) {
            case "": {

                break;
            }

            case "pd": {
                Collections.sort(items, (a, b) -> a.getPrice() < b.getPrice() ? 1 : a.getPrice() == b.getPrice() ? 0 : -1);
                break;
            }

            case "pa": {
                Collections.sort(items, (a, b) -> a.getPrice() < b.getPrice() ? -1 : a.getPrice() == b.getPrice() ? 0 : 1);
                break;
            }

            case "ma": {
                Collections.sort(items, (a, b) -> a.getMark() < b.getMark() ? 1 : a.getMark() == b.getMark() ? 0 : -1);
                break;
            }

            case "az": {
                Collections.sort(items, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
            }
        }

    }

    public String get_item() {

        current_item = null;
        current_mark = null;

        mark = 0;

        new_id_item = item_id;

        if ((Integer) item_id != 0) {
            if (old_id_item != new_id_item) {
                comment_text = "";
            }
            old_id_item = item_id;
            current_item = ItemModel.get_item_by_id(item_id);

            if (current_item == null || current_item.getActive() == 0) {
                return "404";
            } else {
                Company c = CompanyModel.find_company_by_short_name(current_item.getCompany_name());
                current_item.setCompany_full_name(c.getFull_name());
                current_item.setMark(round(current_item.getMark(), 2));

                //quantity
                HttpSession session = util.SessionUtils.getSession();
                Farmer f = (Farmer) session.getAttribute("user");

                current_mark = MarkModel.get_mark_for_item_farmer(current_item.getId_item(), f.getUsername());

                ordered = false;
                List<Order> orders = OrderModel.get_all_orders_for_farmer(f.getUsername());
                for (int i = 0; i < orders.size(); i++) {
                    if (ItemOrderModel.already_ordered_item(current_item.getId_item(), orders.get(i).getId_order())) {
                        ordered = true;
                        break;
                    }
                }

                user_comment = CommentModel.get_comment_for_user_item(current_item.getId_item(), f.getUsername());
                if (user_comment != null) {
                    comments = CommentModel.get_all_comments_for_items_not_user(current_item.getId_item(), f.getUsername());
                    comments.add(0, user_comment);
                } else {
                    comments = CommentModel.get_all_comments_for_items(current_item.getId_item());
                }

                for (int i = 0; i < comments.size(); i++) {
                    Mark m = MarkModel.get_mark_for_item_farmer(comments.get(i).getId_item(), comments.get(i).getFarmer_username());
                    int rate = 0;
                    if (m != null) {
                        rate = m.getMark();
                    }

                    comments.get(i).setMark(rate);
                }

                return "";
            }
        } else {
            return "404";
        }

    }

    public void hej() {

    }

    public void add_to_cart() {
        HttpSession session = util.SessionUtils.getSession();
        Farmer f = (Farmer) session.getAttribute("user");

        Cart current_cart = CartModel.get_cart_for_user(f.getUsername());

        if (current_cart == null) {
            Cart c = new Cart();
            c.setFarmer_username(f.getUsername());
            c.setCart_start_time(LocalDateTime.now());
            CartModel.add_cart_for_user(c);

            current_cart = CartModel.get_cart_for_user(f.getUsername());
        }

        ItemCart current_item_cart = ItemCartModel.get_item_in_cart_for_user(current_cart.getId_cart(), current_item.getId_item());

        if (current_item_cart == null) {
            ItemCart item_cart = new ItemCart();
            item_cart.setId_cart(current_cart.getId_cart());
            item_cart.setId_item(current_item.getId_item());
            item_cart.setAmount(quantity_order);

            ItemCartModel.add_item_to_cart(item_cart);
        } else {
            current_item_cart.setAmount(current_item_cart.getAmount() + quantity_order);

            ItemCartModel.update_item_in_cart(current_item_cart);
        }

        Item item = ItemModel.get_item_by_id(current_item.getId_item());

        item.setAmount(item.getAmount() - quantity_order);

        ItemModel.update_item(item);

        if (item.getAmount() >= 1) {
            quantity_order = 1;
        } else {
            quantity_order = 0;
        }

    }

    public void add_comment() {
        HttpSession session = util.SessionUtils.getSession();
        Farmer f = (Farmer) session.getAttribute("user");

        Comment comm = new Comment();

        comm.setComment(comment_text);
        comm.setFarmer_username(f.getUsername());
        comm.setId_item(current_item.getId_item());

        comm.setDate_of_comment(LocalDate.now());
        CommentModel.add_comment_item_user(comm);

    }

    public UIComponent[] findComponent(final String id) {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        root.visitTree(VisitContext.createVisitContext(FacesContext.getCurrentInstance()), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if (component != null
                        && component.getId() != null
                        && component.getId().equals(id)) {
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return found;

    }

    public void update_mark() {
        HttpSession session = util.SessionUtils.getSession();
        Farmer f = (Farmer) session.getAttribute("user");

        Mark m = new Mark();
        m.setFarmer_username(f.getUsername());
        m.setId_item(current_item.getId_item());
        m.setMark(mark);

        MarkModel.add_mark_for_item_farmer(m);

        List<Mark> marks = MarkModel.get_all_marks_for_item(current_item.getId_item());
        double sum = 0;
        for (int i = 0; i < marks.size(); i++) {
            sum += (double) marks.get(i).getMark();
        }

        current_item.setMark((double) (sum / (double) marks.size()));
        ItemModel.update_item(current_item);
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getMark_int() {
        return mark_int;
    }

    public void setMark_int(Integer mark_int) {
        this.mark_int = mark_int;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public Item getCurrent_item() {
        return current_item;
    }

    public void setCurrent_item(Item current_item) {
        this.current_item = current_item;
    }

    public Mark getCurrent_mark() {
        return current_mark;
    }

    public void setCurrent_mark(Mark current_mark) {
        this.current_mark = current_mark;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getQuantity_order() {
        quantity_order = 1;
        return quantity_order;
    }

    public void setQuantity_order(int quantity_order) {
        this.quantity_order = quantity_order;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Comment getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(Comment user_comment) {
        this.user_comment = user_comment;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

}
