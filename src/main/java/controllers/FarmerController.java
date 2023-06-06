/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.NurseryGarden;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.NurseryGardenModel;
import models.SeedlingInNurseryModel;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class FarmerController {

    private List<NurseryGarden> gardens;
    private NurseryGarden garden;
    private String nursery_name;
    private String nursery_location;
    private int nursery_length;
    private int nursery_width;

   
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
     
    public void get_all_nursery_garden_for_farmer(String username) {
        gardens = NurseryGardenModel.get_all_nursery_gardens_for_user(username);

        for (int i = 0; i < gardens.size(); i++) {
            NurseryGarden ng = gardens.get(i);
            ng.setFree_planting_holes(SeedlingInNurseryModel.get_number_of_free_planting_holes(ng.getId_garden()));
            ng.setNot_free_planting_holes(SeedlingInNurseryModel.get_number_of_planted_seedlings(ng.getId_garden()));
        }

    }

    public void add_nursery(String farmer_name) {

        NurseryGardenModel.add_nursery(farmer_name, nursery_name, nursery_location, nursery_length, nursery_width);
        int max_id = NurseryGardenModel.get_max_id();
        SeedlingInNurseryModel.add_new_seedling_nursery(max_id, 0, nursery_length * nursery_width);
        PrimeFaces.current().dialog().closeDynamic("add_nursery");
        nursery_length = 0;
        nursery_location = null;
        nursery_name = null;
        nursery_width = 0;
    }

    public String find_nursery(int id) {
        NurseryGarden g = NurseryGardenModel.find_nursery_garden(id);
        HttpSession session = util.SessionUtils.getSession();
        session.setAttribute("user_nursery", g);
        return "farmer_nursery?faces-redirect=true";
    }

    public void fireLoginDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("width", 640);
        options.put("headerElement", "Add new Nursery");
        options.put("height", "100%");
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic("add_nursery", options, null);
    }

    public List<NurseryGarden> getGardens() {
        return gardens;
    }

    public void setGardens(List<NurseryGarden> gardens) {
        this.gardens = gardens;
    }

    public NurseryGarden getGarden() {
        return garden;
    }

    public void setGarden(NurseryGarden garden) {
        this.garden = garden;
    }

    public String getNursery_name() {
        return nursery_name;
    }

    public void setNursery_name(String nursery_name) {
        this.nursery_name = nursery_name;
    }

    public String getNursery_location() {
        return nursery_location;
    }

    public void setNursery_location(String nursery_location) {
        this.nursery_location = nursery_location;
    }

    public int getNursery_length() {
        return nursery_length;
    }

    public void setNursery_length(int nursery_length) {
        this.nursery_length = nursery_length;
    }

    public int getNursery_width() {
        return nursery_width;
    }

    public void setNursery_width(int nursery_width) {
        this.nursery_width = nursery_width;
    }

}
