/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;
import models.AdminModel;
import models.NurseryGardenModel;
import util.BackgroundJobManager;

/**
 *
 * @author Aleksandar
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class ManagerBean {

    @EJB
    private BackgroundJobManager backgroundJobManager;

    private String message = null;

    private String admin_username;
    private String farmer_username;
    private String company_name;

    @PostConstruct
    private void init() {
        backgroundJobManager = new BackgroundJobManager();
    }

    public String getMessage() {
        HttpSession session = util.SessionUtils.getSession();
        Farmer farmer = (Farmer) session.getAttribute("user");
        List<NurseryGarden> nurseries;
        int water;
        double temp;

        int num;
        StringBuilder sb;
        if (farmer != null) {
            nurseries = NurseryGardenModel.get_all_nursery_gardens_for_user(farmer.getUsername());
            sb = new StringBuilder();
            num = 0;
            for (int i = 0; i < nurseries.size(); i++) {
                NurseryGarden nursery = nurseries.get(i);
                water = nursery.getAmount_of_water();
                temp = nursery.getTemperature();

                if (water < 75 || temp < 12) {
                    num++;
                    if (num > 1) {
                        sb.append(", \"").append(nursery.getName()).append("\"");
                    } else {
                        sb.append("\"").append(nursery.getName()).append("\"");
                    }
                }
            }

            if (num == 0) {
                message = null;
            } else if (num == 1) {
                message = "Nursery " + sb.toString() + " requires maintenance";
            } else {
                message = "Nurseries " + sb.toString() + " require maintenance";
            }
        } else {
            message = null;
        }

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdmin_username() {
        HttpSession session = util.SessionUtils.getSession();
        Admin a = (Admin) session.getAttribute("admin");

        return a.getAdmin_username();
    }

    public void setAdmin_username(String admin_username) {
        this.admin_username = admin_username;
    }

    public String getFarmer_username() {
        HttpSession session = util.SessionUtils.getSession();
        Farmer f = (Farmer) session.getAttribute("user");

        return f.getUsername();
    }

    public void setFarmer_username(String farmer_username) {
        this.farmer_username = farmer_username;
    }

    public String getCompany_name() {
        HttpSession session = util.SessionUtils.getSession();
        Company c = (Company) session.getAttribute("company");

        return c.getShort_name();
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

}
