/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.AgentsForSeedling;
import beans.Farmer;
import beans.Item;
import beans.ItemOrder;
import beans.NurseryGarden;
import beans.Order;
import beans.SeedlingInNursery;
import beans.Storage;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.AgentsForSeedlingModel;
import models.FriendlySeedlingModel;
import models.ItemModel;
import models.ItemOrderModel;
import models.NurseryGardenModel;
import models.OrderModel;
import models.SeedlingInNurseryModel;
import org.primefaces.PrimeFaces;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.progressbar.ProgressBar;
import org.primefaces.component.tooltip.Tooltip;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import models.StorageModel;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class NurseryController implements Serializable {

    DashboardModel model;
    NurseryGarden nursery;
    Dashboard dashboard;
    SeedlingInNursery seeds_in_nursery[];
    Item seeds[];
    int times = 0;
    List<Item> agents;
    Item selected_agent;

    List<Item> seedlings;
    Item selected_seedling;

    SeedlingInNursery current_seedling;
    private double temperature;
    private int water;

    private int id_nursery;
    private List<Item> items_in_storage;
    private List<Item> ordered_items;
    private List<Item> filtered_items;

    private Item selected_item;

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

    public void init() {

        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");
        if (ng != null) {
            id_nursery = ng.getId_garden();
            nursery = NurseryGardenModel.find_nursery_garden(id_nursery);
            water = nursery.getAmount_of_water();
            temperature = nursery.getTemperature();
            FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            if (model != null) {
                for (int i = 0; i < model.getColumnCount(); i++) {
                    for (int j = 0; j < model.getColumn(i).getWidgetCount(); j++) {
                        int num = j * nursery.getLength() + i + 1;
                        model.getColumn(i).removeWidget("plot_" + num);
                    }
                }
            }
            model = new DefaultDashboardModel();
            dashboard = (Dashboard) findComponent("board")[0];
            dashboard.getChildren().clear();
            UIViewRoot root = context.getViewRoot();
            /*Poll poll = (Poll) findComponent("poll")[0];

        MethodExpression method1 = application.getExpressionFactory().createMethodExpression(
                context.getELContext(), "#{nurseryController.refresh}", Void.class, new Class[]{});

        poll.setListener(method1);*/

            seeds_in_nursery = new SeedlingInNursery[nursery.getLength() * nursery.getWidth()];
            seeds = new Item[nursery.getLength() * nursery.getWidth()];

            for (int i = 0; i < nursery.getLength(); i++) {
                DashboardColumn column = new DefaultDashboardColumn();
                for (int j = 0; j < nursery.getWidth(); j++) {
                    int num = i + j * nursery.getLength() + 1;
                    SeedlingInNursery sin = SeedlingInNurseryModel.find_plot_for_nursery(id_nursery, num);
                    Item seed = new Item();

                    if (sin.getId_seedling() != 0) {
                        seed = ItemModel.get_item_by_id(sin.getId_seedling());
                    }

                    Panel panel_old = (Panel) findComponent("plot_" + num)[0];
                    if (panel_old != null) {
                        root.removeComponentResource(context, panel_old);
                    }
                    Panel p = (Panel) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");

                    int width = nursery.getLength() * 187;
                    dashboard.setStyle("width:" + width + "px; text-align: center; align-content: center; margin: auto !important;");
                    width = 145;
                    int height = 110;
                    String border = "border-radius: 20px;";

                    p.setStyle("width:" + width + "px;height:" + height + "px;" + border + "margin:20px; display:block; text-align:center; border-style: none !important;");
                    p.setId("plot_" + num);
                    p.setInView(false);
                    long minutes = 0, hours = 0;
                    CommandButton b2 = null;
                    if (sin.getActive() == 1) {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime harvested_time = sin.getHarvested_time();

                        LocalDateTime free_time = harvested_time.plusDays(1);
                        long diff = harvested_time.until(now, ChronoUnit.DAYS);
                        minutes = now.until(free_time, ChronoUnit.MINUTES);
                        hours = now.until(free_time, ChronoUnit.HOURS);

                        minutes = minutes - hours * 60;

                        if (diff > 0) {
                            sin.setActive(0);
                            SeedlingInNurseryModel.change_active_type(sin);
                        }
                    }
                    seeds_in_nursery[num - 1] = sin;
                    seeds[num - 1] = seed;
                    switch (sin.getActive()) {
                        case 0:
                            p.setStyleClass("box");
                            CommandButton b_old = (CommandButton) findComponent("plot_" + num + "_button")[0];
                            if (b_old != null) {
                                root.removeComponentResource(context, b_old);
                            }

                            CommandButton b = (CommandButton) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.CommandButton", "org.primefaces.component.CommandButtonRenderer");
                            b.setValue("ADD SEED");

                            b.setStyle("font-size: large; font-weight:bold; height: 110px; width: 100%; padding: 0px; border-radius: 20px;");
                            b.setStyleClass("greenButton");
                            b.setId("plot_" + num + "_button");

                            MethodExpression method = application.getExpressionFactory().createMethodExpression(
                                    context.getELContext(), "#{nurseryController.fireSeedlingDialog(" + num + ")}", Void.class, new Class[]{Integer.class});

                            b.setActionExpression(method);

                            MethodExpression method_ajax = application.getExpressionFactory().createMethodExpression(
                                    context.getELContext(), "#{nurseryController.refresh}", null, new Class[]{});

                            AjaxBehavior ajax = (AjaxBehavior) application.createBehavior(AjaxBehavior.BEHAVIOR_ID);
                            ajax.setUpdate("board scroll");

                            ajax.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(method_ajax, method_ajax));

                            b.addClientBehavior("dialogReturn", ajax);

                            p.getChildren().add(b);

                            //<p:ajax event="dialogReturn" listener="#{farmerController.get_all_nursery_garden_for_farmer(user.username)}" update="datatable_overlay output_label" />
                            break;
                        case 1:
                            p.setStyleClass("box");

                            CommandButton b1_old = (CommandButton) findComponent("plot_" + num + "_button")[0];
                            if (b1_old != null) {
                                root.removeComponentResource(context, b1_old);
                            }

                            CommandButton b1 = (CommandButton) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.CommandButton", "org.primefaces.component.CommandButtonRenderer");
                            if (hours < 10 && minutes < 10) {
                                b1.setValue("0" + hours + " : " + "0" + minutes);
                            } else if (hours < 10) {
                                b1.setValue("0" + hours + " : " + minutes);
                            } else if (minutes < 10) {
                                b1.setValue("" + hours + " : " + "0" + minutes);
                            } else {
                                b1.setValue("" + hours + " : " + minutes);
                            }
                            b1.setStyle("font-size: large; font-weight:bold; height: 110px; width: 100%; padding: 0px; border-radius: 20px;");
                            b1.setStyleClass("redButton");
                            b1.setId("plot_" + num + "_button");
                            p.getChildren().add(b1);
                            break;
                        case 2:

                            p.setStyleClass("box");

                            CommandButton b2_old = (CommandButton) findComponent("plot_" + num + "_button")[0];
                            if (b2_old != null) {
                                root.removeComponentResource(context, b2_old);
                            }
                            b2 = (CommandButton) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.CommandButton", "org.primefaces.component.CommandButtonRenderer");
                            b2.setValue(seed.getName().toUpperCase());
                            b2.setStyle("font-size: large; font-weight:bold; height: 110px; width: 100%; padding: 0px; border-radius: 20px");
                            b2.setStyleClass("orangeButton");
                            b2.setId("plot_" + num + "_button");

                            MethodExpression method_b2 = application.getExpressionFactory().createMethodExpression(
                                    context.getELContext(), "#{nurseryController.fireAgentDialog(" + num + ")}", Void.class, new Class[]{Integer.class});

                            b2.setActionExpression(method_b2);

                            MethodExpression method_b2_ajax = application.getExpressionFactory().createMethodExpression(
                                    context.getELContext(), "#{nurseryController.refresh}", null, new Class[]{});

                            AjaxBehavior ajax_b2 = (AjaxBehavior) application.createBehavior(AjaxBehavior.BEHAVIOR_ID);
                            ajax_b2.setUpdate("board scroll");

                            ajax_b2.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(method_b2_ajax, method_b2_ajax));

                            b2.addClientBehavior("dialogReturn", ajax_b2);
                            p.getChildren().add(b2);
                            break;
                        default:
                            break;
                    }

                    dashboard.getChildren().add(p);

                    column.addWidget("plot_" + num);

                    Tooltip t = (Tooltip) findComponent("plot_" + num + "_tooltip")[0];
                    if (t != null) {
                        root.removeComponentResource(context, t);
                    }
                    Tooltip tool = (Tooltip) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.Tooltip", "org.primefaces.component.TooltipRenderer");
                    tool.setFor("plot_" + num);
                    tool.setShowEvent("mouseover");
                    tool.setHideEvent("mouseout");
                    tool.setId("plot_" + num + "_tooltip");

                    p.getChildren().add(tool);

                    switch (sin.getActive()) {
                        case 0: {

                            break;
                        }
                        case 2:
                            OutputLabel ol_name = (OutputLabel) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.OutputLabel", "org.primefaces.component.OutputLabelRenderer");
                            ol_name.setValue("Name: " + seed.getName());
                            ol_name.setStyle("text-weight: bold; font-size:large; display: block");

                            OutputLabel ol_company = (OutputLabel) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.OutputLabel", "org.primefaces.component.OutputLabelRenderer");
                            ol_company.setValue("Company: " + seed.getCompany_name());

                            ol_company.setStyle("text-weight: bold; font-size:large; display: block");
                            ProgressBar pb = (ProgressBar) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.ProgressBar", "org.primefaces.component.ProgressBarRenderer");
                            pb.setDisplayOnly(true);
                            LocalDateTime now = LocalDateTime.now();
                            LocalDateTime planted_time = sin.getPlanted_time();

                            List<beans.AgentsForSeedling> agents;
                            agents = AgentsForSeedlingModel.get_agents_for_seedling(sin.getId_seedling_nursery());

                            long needed_time = seed.getDays() * 1440;
                            long reduced_days = 0;
                            long days_in_plot;
                            for (int k = 0; k < agents.size(); k++) {
                                Item it = ItemModel.get_item_by_id(agents.get(k).getId_item());
                                reduced_days += it.getDays();
                            }

                            //planted_time = planted_time.plusDays(reduced_days);
                            days_in_plot = planted_time.until(now, ChronoUnit.MINUTES);

                            long final_days_passed = reduced_days * 1440 + days_in_plot;

                            int value;

                            if (needed_time > final_days_passed) {
                                value = (int) ((final_days_passed * 100) / needed_time);
                            } else {
                                value = 100;
                            }
                            pb.setValue(value);
                            pb.setLabelTemplate("{value}%");
                            if (value < 100) {
                                tool.getChildren().add(ol_name);
                                tool.getChildren().add(ol_company);
                                tool.getChildren().add(pb);
                            } else {
                                OutputLabel ol_ready = (OutputLabel) application.createComponent(FacesContext.getCurrentInstance(), "org.primefaces.component.OutputLabel", "org.primefaces.component.OutputLabelRenderer");
                                ol_ready.setValue("Plant is ready for harvest!");
                                ol_ready.setStyle("text-weight: bold; font-size:large; display: block");
                                if (b2 != null) {
                                    b2.setStyle("font-size: large; font-weight:bold; height: 110px; width: 100%; padding: 0px; border-radius: 20px; border: 5px solid black");

                                    MethodExpression method_b2 = application.getExpressionFactory().createMethodExpression(
                                            context.getELContext(), "#{nurseryController.refresh}", Void.class, new Class[]{Integer.class});

                                    b2.setActionExpression(method_b2);

                                    MethodExpression method_b2_ajax = application.getExpressionFactory().createMethodExpression(
                                            context.getELContext(), "#{nurseryController.remove_seedling_from_plot(" + num + ")}", null, new Class[]{});

                                    AjaxBehavior ajax_b2 = (AjaxBehavior) application.createBehavior(AjaxBehavior.BEHAVIOR_ID);
                                    ajax_b2.setUpdate("board scroll");

                                    ajax_b2.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(method_b2_ajax, method_b2_ajax));

                                    b2.addClientBehavior("click", ajax_b2);
                                }
                                tool.getChildren().add(ol_ready);
                            }

                            break;
                        case 1: {

                            break;
                        }
                        default:
                            break;
                    }

                }
                model.addColumn(column);

            }
        }
    }

    //moze da se koristi sa poll-om
    public void refresh() {
        //hej
    }

    public void remove_seedling_from_plot(int plot_id) {
        SeedlingInNursery seed = seeds_in_nursery[plot_id - 1];
        List<AgentsForSeedling> agents = AgentsForSeedlingModel.get_agents_for_seedling(seed.getId_seedling_nursery());
        AgentsForSeedlingModel.delete_agents_for_seedling(agents);
        seed.setActive(1);
        seed.setHarvested_time(LocalDateTime.now());
        seed.setPlanted_time(null);
        seed.setId_seedling(0);
        SeedlingInNurseryModel.remove_seedling_for_plot(seed);
        init();
    }

    public void fireAgentDialog(int plot_id) {
        current_seedling = seeds_in_nursery[plot_id - 1];
        get_all_agents_for_nursery();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("width", 800);
        options.put("headerElement", "Add Agent");
        options.put("height", "100%");
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic("add_agent", options, null);
    }

    public void get_all_agents_for_nursery() {
        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");
        int id_nursery = ng.getId_garden();
        List<Storage> storage = StorageModel.get_all_items_for_nursery(id_nursery);
        agents = new LinkedList<>();

        for (int i = 0; i < storage.size(); i++) {
            int id = storage.get(i).getId_item();
            Item item = ItemModel.get_item_by_id(id);
            if (item != null && item.getType() == 'A') {
                item.setAmount_in_storage(storage.get(i).getAmount());
                agents.add(item);
            }
        }
    }

    public void confirm_agent_for_seed() {
        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");
        if (selected_agent != null) {

            AgentsForSeedlingModel.add_agent_seedling(selected_agent.getId_item(), current_seedling.getId_seedling_nursery());
            StorageModel.decrease_amount_for_item(ng.getId_garden(), selected_agent.getId_item());
            selected_agent = null;
        }
        PrimeFaces.current().dialog().closeDynamic("add_agent");
    }

    public void fireSeedlingDialog(int plot_id) {
        current_seedling = seeds_in_nursery[plot_id - 1];
        get_all_seedlings_for_nursery();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("width", 640);
        options.put("headerElement", "Add Seedling");
        options.put("height", "100%");
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic("add_seedling", options, null);
    }

    public void get_all_seedlings_for_nursery() {
        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");
        int id_nursery = ng.getId_garden();
        List<Storage> storage = StorageModel.get_all_items_for_nursery(id_nursery);
        seedlings = new LinkedList<>();

        for (int i = 0; i < storage.size(); i++) {
            int id = storage.get(i).getId_item();
            Item item = ItemModel.get_item_by_id(id);
            if (item != null && item.getType() == 'S') {
                item.setAmount_in_storage(storage.get(i).getAmount());
                seedlings.add(item);
            }
        }
    }

    public void confirm_seedling_for_plot() {
        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");

        if (selected_seedling != null && current_seedling != null && is_seedling_friendly(ng.getLength(), ng.getWidth(), current_seedling, selected_seedling.getId_item())) {

            current_seedling.setId_seedling(selected_seedling.getId_item());
            current_seedling.setPlanted_time(LocalDateTime.now());
            current_seedling.setActive(2);
            SeedlingInNurseryModel.add_seedling_for_plot(current_seedling);
            StorageModel.decrease_amount_for_item(ng.getId_garden(), selected_seedling.getId_item());
            selected_seedling = null;

        }
        current_seedling = null;
        PrimeFaces.current().dialog().closeDynamic("add_seedling");
    }

    private boolean is_seedling_friendly(int len, int wid, SeedlingInNursery seed, int id_seedling) {
        int seed_plot_num = seed.getPlot_number();
        int col = seed_plot_num % len;
        int row = (seed_plot_num - 1) / len + 1;

        if (col == 0) {
            col = len;
        }
        if (col > 1) {
            SeedlingInNursery seedling = seeds_in_nursery[(row - 1) * len + col - 1 - 1];
            if (seedling.getActive() == 2) {
                if (id_seedling != seedling.getId_seedling() && !FriendlySeedlingModel.are_seedlings_friendly(id_seedling, seedling.getId_seedling())) {
                    System.out.println("NE MOZE(col>1) => curr_seed_id: " + seed.getId_seedling() + ", friend_seed_id: " + seedling.getId_seedling());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "This seedling can't be in this plot!", "This seedling can't be in this plot!"));
                    return false;
                }
            }
        }

        if (col < len) {
            SeedlingInNursery seedling = seeds_in_nursery[(row - 1) * len + col + 1 - 1];
            if (seedling.getActive() == 2) {
                if (id_seedling != seedling.getId_seedling() && !FriendlySeedlingModel.are_seedlings_friendly(id_seedling, seedling.getId_seedling())) {
                    System.out.println("NE MOZE(col<len) => curr_seed_id: " + seed.getId_seedling() + ", friend_seed_id: " + seedling.getId_seedling());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "This seedling can't be in this plot!", "This seedling can't be in this plot!"));
                    return false;
                }
            }
        }

        if (row > 1) {
            SeedlingInNursery seedling = seeds_in_nursery[(row - 2) * len + col - 1];
            if (seedling.getActive() == 2) {
                if (id_seedling != seedling.getId_seedling() && !FriendlySeedlingModel.are_seedlings_friendly(id_seedling, seedling.getId_seedling())) {
                    System.out.println("NE MOZE(row>1) => curr_seed_id: " + seed.getId_seedling() + ", friend_seed_id: " + seedling.getId_seedling());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "This seedling can't be in this plot!", "This seedling can't be in this plot!"));
                    return false;
                }
            }
        }

        if (row < wid) {
            SeedlingInNursery seedling = seeds_in_nursery[row * len + col - 1];
            if (seedling.getActive() == 2) {
                if (id_seedling != seedling.getId_seedling() && !FriendlySeedlingModel.are_seedlings_friendly(id_seedling, seedling.getId_seedling())) {
                    System.out.println("NE MOZE(row<wid) => curr_seed_id: " + seed.getId_seedling() + ", friend_seed_id: " + seedling.getId_seedling());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "This seedling can't be in this plot!", "This seedling can't be in this plot!"));
                    return false;
                }
            }
        }

        return true;
    }

    public void update_nursery(String type) {
        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");

        if (type.equals("w")) {
            ng.setFarmer_informed_w(0);
        }

        if (type.equals("t")) {
            ng.setFarmer_informed_t(0);
        }

        ng.setAmount_of_water(water);
        ng.setTemperature(temperature);

        session.setAttribute("user_nursery", ng);

        NurseryGardenModel.update_garden(ng);

    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public void cancel_order(int id_item_order) {
        ItemOrder it = ItemOrderModel.get_item_order_by_id(id_item_order);
        it.setStatus('c');
        ItemOrderModel.update_item_order(it);

        Item item = ItemModel.get_item_by_id(it.getId_item());
        item.setAmount(item.getAmount() + it.getAmount());
        ItemModel.update_item(item);

        List<ItemOrder> items = ItemOrderModel.get_items_for_order(it.getId_order());

        Order o = OrderModel.find_order_by_id(it.getId_order());
        o.setPrice(o.getPrice() - it.getPrice());

        o.setPrice(round(o.getPrice(), 2));
        
        boolean all_declined = true;
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getStatus() != 'd' && items.get(i).getStatus() != 'c') {
                all_declined = false;
                break;
            }
        }

        if (all_declined) {

            o.setPriority(0);
            o.setStatus('c');

        }

        OrderModel.update_order(o);

    }

    public void get_items_for_storage() {
        List<Storage> storage_items = StorageModel.get_all_items_for_nursery(id_nursery);
        items_in_storage = new LinkedList<>();
        for (int i = 0; i < storage_items.size(); i++) {
            Item it = ItemModel.get_item_by_id(storage_items.get(i).getId_item());
            it.setAmount_in_storage(storage_items.get(i).getAmount());
            it.setStatus('A');
            items_in_storage.add(it);
        }

        HttpSession session = util.SessionUtils.getSession();
        NurseryGarden ng = (NurseryGarden) session.getAttribute("user_nursery");

        if (ng != null) {
            List<ItemOrder> orders = ItemOrderModel.get_all_orders_for_nursery(ng.getId_garden());
            for (int i = 0; i < orders.size(); i++) {
                ItemOrder it = orders.get(i);
                Item item = ItemModel.get_item_by_id(it.getId_item());
                item.setAmount_in_storage(it.getAmount());

                switch (it.getStatus()) {
                    case 'o':
                    case 'w':
                        item.setStatus('O');
                        item.setItem_order_id(it.getId_item_order());
                        break;
                    case 'g':
                        item.setStatus('G');
                        break;
                    default:
                        item.setStatus('S');
                        break;
                }
                items_in_storage.add(item);
            }
        }

        //ovo mora da se menja
        /*
        if (f != null) {
            List<Order> orders = OrderModel.get_arrived_orders_for_farmer(f.getUsername());
            for (int i = 0; i < orders.size(); i++) {

                List<ItemOrder> items = ItemOrderModel.get_items_for_order(orders.get(i).getId_order());
                for (int j = 0; j < items.size(); j++) {
                    Item it = ItemModel.get_item_by_id(items.get(j).getId_item());
                    it.setAmount_in_storage(items.get(j).getAmount());
                    it.setArrived(false);
                    items_in_storage.add(it);
                }

            }
        }*/
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
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

    public List<Item> getAgents() {
        return agents;
    }

    public void setAgents(List<Item> agents) {
        this.agents = agents;
    }

    public Item getSelected_agent() {
        return selected_agent;
    }

    public void setSelected_agent(Item selected_agent) {
        this.selected_agent = selected_agent;
    }

    public List<Item> getSeedlings() {
        return seedlings;
    }

    public void setSeedlings(List<Item> seedlings) {
        this.seedlings = seedlings;
    }

    public Item getSelected_seedling() {
        return selected_seedling;
    }

    public void setSelected_seedling(Item selected_seedling) {
        this.selected_seedling = selected_seedling;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public List<Item> getItems_in_storage() {
        return items_in_storage;
    }

    public void setItems_in_storage(List<Item> items_in_storage) {
        this.items_in_storage = items_in_storage;
    }

    public List<Item> getOrdered_items() {
        return ordered_items;
    }

    public void setOrdered_items(List<Item> ordered_items) {
        this.ordered_items = ordered_items;
    }

    public List<Item> getFiltered_items() {
        return filtered_items;
    }

    public void setFiltered_items(List<Item> filtered_items) {
        this.filtered_items = filtered_items;
    }

    public Item getSelected_item() {
        return selected_item;
    }

    public void setSelected_item(Item selected_item) {
        this.selected_item = selected_item;
    }

}
