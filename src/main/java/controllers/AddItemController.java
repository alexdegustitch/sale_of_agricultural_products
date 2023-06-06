/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Admin;
import beans.Company;
import beans.FriendlySeedling;
import beans.Item;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.FriendlySeedlingModel;
import models.ItemModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@ViewScoped
public class AddItemController {

    private String name;
    private int days = 1;
    private int amount = 1;
    private double price = 1;
    private String description;
    private List<Item> chosen_seedlings;
    private List<Integer> chosen_seedlings_id;
    private List<Item> seedlings;
    private char type;
    private boolean all_selected;

    private UploadedFile file;
    private File LOCATION = new File("C:\\pictures");
    private UploadedFile json_file;

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
        } else if (type.equals("a")) {

            try {
                context.getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("f")) {
            try {
                context.getExternalContext().redirect("farmer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void get_all_seedlings() {

        HttpSession session = util.SessionUtils.getSession();
        String type = (String) session.getAttribute("type");

        FacesContext context = FacesContext.getCurrentInstance();
        if (type == null) {
            try {
                context.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("a")) {

            try {
                context.getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("f")) {
            try {
                context.getExternalContext().redirect("farmer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        seedlings = ItemModel.get_all_active_seedlings();
    }

    public void select_all() {
        if (all_selected) {
            //chosen_seedlings = ItemModel.get_all_active_seedlings();
            chosen_seedlings_id = new LinkedList<>();
            for (int i = 0; i < seedlings.size(); i++) {
                chosen_seedlings_id.add(seedlings.get(i).getId_item());
            }
        } else {
            //chosen_seedlings = null;
            chosen_seedlings_id = new LinkedList<>();
        }
    }

    public String onFlowProcess(FlowEvent event) {

        if (amount <= 0) {
            FacesContext.getCurrentInstance().addMessage("myForm:quantity",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quanity must be greater than zero.", "Quanity must be greater than zero."));
        }

        if (days <= 0) {
            FacesContext.getCurrentInstance().addMessage("myForm:days",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Days to ripen/Accelerating growth in days must be greater than zero.", "Days must be greater than zero."));
        }

        if (price <= 0) {
            FacesContext.getCurrentInstance().addMessage("myForm:price",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Price must be greater than zero.", "Price must be greater than zero."));
        }

        if (price <= 0 || amount <= 0 || days <= 0) {
            return event.getOldStep();
        }
        return event.getNewStep();

    }

    public String add_item() {

        HttpSession session = util.SessionUtils.getSession();
        Company company = (Company) session.getAttribute("company");

        Item item_exists = ItemModel.get_item_by_name_and_company(name, company.getShort_name());

        if (item_exists != null) {
            FacesContext.getCurrentInstance().addMessage("myForm:file_upload",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Product with the given name already exists for your company!", "Product with the given name already exists for your company!"));
            return null;
        }
        Item item = new Item();

        item.setActive(1);
        item.setAmount(amount);
        item.setCompany_name(company.getShort_name());
        item.setDays(days);
        item.setDescription(description);
        item.setMark(0);
        item.setName(name);
        item.setPrice(price);
        item.setType(type);

        ItemModel.add_new_item(item);

        int id1 = ItemModel.get_item_max_id();

        if (chosen_seedlings != null) {
            for (int i = 0; i < chosen_seedlings.size(); i++) {
                int id2 = chosen_seedlings.get(i).getId_item();
                FriendlySeedling fs = new FriendlySeedling();
                fs.setId_seedling1(id1);
                fs.setId_seedling2(id2);

                FriendlySeedlingModel.add_new_friendly_seedling(fs);

                fs = new FriendlySeedling();
                fs.setId_seedling1(id2);
                fs.setId_seedling2(id1);

                FriendlySeedlingModel.add_new_friendly_seedling(fs);
            }
        }

        if (file != null) {
            try {

                //File myTempFile = new File(com.google.common.io.Files.createTempDir(), "MySpecificName." + sufix);
                //File save = File.createTempFile("item_" + max_id, "." + sufix, LOCATION);
                File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                while (f != null && !f.getName().equals("WEB-INF")) {
                    f = f.getParentFile();
                }
                f = f.getParentFile();

                String prefix = FilenameUtils.getBaseName(file.getFileName());
                String sufix = FilenameUtils.getExtension(file.getFileName());

                if (sufix.equals("jpg") || sufix.equals("jpeg") || sufix.equals("png")) {
                    LOCATION = new File(f.getAbsolutePath() + "\\resources\\icarus-layout\\images\\items");

                    System.out.println(LOCATION.getAbsolutePath());
                    File save = new File(LOCATION, "item_" + id1 + "." + sufix);
                    Files.write(save.toPath(), file.getContent());
                }

            } catch (IOException | URISyntaxException ex) {
                FacesMessage msg = new FacesMessage("Error!", "Error occured while adding image!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger
                        .getLogger(AddItemController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }
        }

        //FacesMessage msg = new FacesMessage("Successful", "Item " + item.getName() + " has been successfully added!");
        //FacesContext.getCurrentInstance().addMessage(null, msg);
        System.out.println(id1);
        return "item_info_company?faces-redirect=true&id=" + id1;

    }

    public void update_image(int id_item) {
        if (file != null) {
            try {

                //File myTempFile = new File(com.google.common.io.Files.createTempDir(), "MySpecificName." + sufix);
                //File save = File.createTempFile("item_" + max_id, "." + sufix, LOCATION);
                File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                while (f != null && !f.getName().equals("WEB-INF")) {
                    f = f.getParentFile();
                }
                f = f.getParentFile();

                String prefix = FilenameUtils.getBaseName(file.getFileName());
                String sufix = FilenameUtils.getExtension(file.getFileName());

                if (sufix == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must attach file!", "You must attach file!"));
                    return;
                }

                if (sufix.equals("jpg") || sufix.equals("jpeg") || sufix.equals("png")) {
                    LOCATION = new File(f.getAbsolutePath() + "\\resources\\icarus-layout\\images\\items");

                    System.out.println(LOCATION.getAbsolutePath());
                    File save = new File(LOCATION, "item_" + id_item + "." + sufix);
                    Files.write(save.toPath(), file.getContent());
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Files with type ." + sufix + " are not allowed!", "Files with type ." + sufix + " are not allowed!"));
                }

            } catch (IOException | URISyntaxException ex) {
                FacesMessage msg = new FacesMessage("Error!", "Error occured while adding image!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger
                        .getLogger(AddItemController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getPath() throws UnsupportedEncodingException {

        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String pathArr[] = fullPath.split("/WEB-INF/classes/");
        System.out.println(fullPath);
        System.out.println(pathArr[0]);
        fullPath = pathArr[0];

        return fullPath;

    }

    public void upload() {
        System.out.println("Ispis: " + json_file);
        if (json_file != null) {
            Reader reader = null;
            try {
                String sufix = FilenameUtils.getExtension(json_file.getFileName());
                System.out.println(sufix);
                if (sufix == null) {
                    System.out.println("sufix: " + sufix);
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must attach file!", "You must attach file!"));
                    return;
                }
                if (!sufix.equals("json")) {
                    System.out.println("sufix: " + sufix);
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "File must be JSON file!", "File must be JSON file!"));
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(json_file.getInputStream(), "UTF-8"));
                // reader = new InputStreamReader(json_file.getInputStream());
                StringBuilder sb = new StringBuilder();

                while (true) {
                    int ch = reader.read();
                    if (ch == -1) {
                        break;
                    }
                    sb.append((char) ch);
                }

                String result = sb.toString();

                //System.out.println(result);
                JSONParser parser = new JSONParser();

                Object obj = parser.parse(result);
                JSONObject jsonobj = (JSONObject) obj;

                long days_obj = 0;
                long amount_obj = 0;
                double price_obj = 0;

                String name_obj = (String) jsonobj.get("Name");

                Object object_days = jsonobj.get("Days");
                if (object_days != null) {
                    days_obj = (long) jsonobj.get("Days");
                }
                String type_obj = (String) jsonobj.get("Type");
                String desc_obj = (String) jsonobj.get("Description");

                Object object_quantity = jsonobj.get("Quantity");
                if (object_quantity != null) {
                    amount_obj = (long) jsonobj.get("Quantity");

                }

                Object object_price = jsonobj.get("Price");
                if (object_price != null) {
                    price_obj = (double) jsonobj.get("Price");
                }
                String image_obj = (String) jsonobj.get("Image");

                String message;
                boolean ok = true;

                StringBuilder sb_message = new StringBuilder();

                if (name_obj == null) {
                    ok = false;
                    message = "Product name is required." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                }

                if (object_days == null) {
                    ok = false;
                    message = "Days to ripen/Accelerating growth in days is required." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));

                } else if (days_obj < 1) {
                    ok = false;
                    message = "Days to ripen/Accelerating growth in days must be greater than zero." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                }

                if (type_obj == null) {
                    ok = false;
                    message = "Product type is required." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                } else if (!type_obj.equals("S") && !type_obj.equals("A")) {
                    ok = false;
                    message = "Product type " + type_obj + " is unknown." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                }

                if (desc_obj == null) {
                    ok = false;
                    message = "Description is required." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                }

                if (object_quantity == null) {
                    ok = false;
                    message = "Quantity is required." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                } else if (amount_obj < 1) {
                    ok = false;
                    message = "Quantity must be greater than zero." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                }

                if (object_price == null) {
                    ok = false;
                    message = "Price is required." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                } else if (price_obj < 1) {
                    ok = false;
                    message = "Price must be greater than zero." + System.getProperty("line.separator");
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                }

                if (!ok) {
                    System.out.println("sufix: " + sufix);

                    return;
                }

                //dodavanje item-a u bazu
                HttpSession session = util.SessionUtils.getSession();
                Company company = (Company) session.getAttribute("company");

                Item item_exists = ItemModel.get_item_by_name_and_company(name_obj, company.getShort_name());

                if (item_exists != null) {
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Product with the given name already exists for your company!", "Product with the given name already exists for your company!"));
                    return;
                }

                Item item = new Item();

                item.setActive(1);
                item.setAmount((int) amount_obj);
                item.setCompany_name(company.getShort_name());
                item.setDays((int) days_obj);
                item.setDescription(desc_obj);
                item.setMark(0);
                item.setName(name_obj);
                item.setPrice(price_obj);
                item.setType(type_obj.charAt(0));

                ItemModel.add_new_item(item);

                int id1 = ItemModel.get_item_max_id();

                if (type_obj.charAt(0) == 'S') {
                    JSONArray friendly_seedlings = (JSONArray) jsonobj.get("FriendlySeedlings");

                    if (friendly_seedlings != null) {
                        for (int i = 0; i < friendly_seedlings.size(); i++) {
                            JSONObject friend_seed = (JSONObject) friendly_seedlings.get(i);
                            String seed_name = (String) friend_seed.get("SeedlingName");
                            String company_name = (String) friend_seed.get("CompanyName");

                            if (seed_name != null && company_name != null) {
                                Item item_2 = ItemModel.get_item_by_name_and_company(seed_name, company_name);
                                int id2 = item_2.getId_item();

                                FriendlySeedling fs = new FriendlySeedling();
                                fs.setId_seedling1(id1);
                                fs.setId_seedling2(id2);

                                FriendlySeedlingModel.add_new_friendly_seedling(fs);

                                fs = new FriendlySeedling();
                                fs.setId_seedling1(id2);
                                fs.setId_seedling2(id1);

                                FriendlySeedlingModel.add_new_friendly_seedling(fs);
                            } else {
                                Item item_delete = ItemModel.get_item_by_id(id1);
                                ItemModel.delete_item(item_delete);

                                List<FriendlySeedling> delete_friendly = FriendlySeedlingModel.get_seedlings_id1(id1);
                                for (int l = 0; l < delete_friendly.size(); l++) {
                                    FriendlySeedlingModel.delete_friendly_seedlings(delete_friendly.get(l));
                                }

                                delete_friendly = FriendlySeedlingModel.get_seedlings_id2(id1);
                                for (int l = 0; l < delete_friendly.size(); l++) {
                                    FriendlySeedlingModel.delete_friendly_seedlings(delete_friendly.get(l));
                                }

                                FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was some error in 'Friendly Seedlings' part of file!", "There was some error in 'Friendly Seedlings' part of file!"));

                                return;
                            }
                        }
                    }
                }

                if (image_obj != null) {
                    System.out.println("image direct: " + image_obj);
                    System.out.println("desc: " + desc_obj);

                    File image_file = new File(image_obj);
                    if (!image_file.exists()) {
                        FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "No Image file found!", "No Image file found!"));
                        FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Product is added!", "Product is added!"));

                        return;
                    }

                    FileInputStream inputStream = new FileInputStream(image_file);

                    File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

                    while (f != null && !f.getName().equals("WEB-INF")) {
                        f = f.getParentFile();
                    }
                    f = f.getParentFile();

                    LOCATION = new File(f.getAbsolutePath() + "\\resources\\icarus-layout\\images\\items\\");

                    FileOutputStream outputStream = new FileOutputStream(LOCATION + "\\item_" + id1 + ".jpg");

                    System.out.println("output: " + LOCATION + "item_" + id1 + ".jpg");
                    System.out.println("input: " + image_obj);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                        System.out.println("buff: " + buffer);
                    }

                    inputStream.close();
                    FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Product is added!", "Product is added!"));
                }
                /*
                JSONArray dist = (JSONArray) jsonobj.get("rows");
                JSONObject obj2 = (JSONObject) dist.get(0);
                JSONArray disting = (JSONArray) obj2.get("elements");
                JSONObject obj3 = (JSONObject) disting.get(0);
                JSONObject obj4 = (JSONObject) obj3.get("distance");
                JSONObject obj5 = (JSONObject) obj3.get("duration");*/

            } catch (IOException | ParseException ex) {
                FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was some error while reading file!", "There was some error while reading file!"));

                Logger
                        .getLogger(AddItemController.class
                                .getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(AddItemController.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();

                    }
                } catch (IOException ex) {
                    Logger.getLogger(AddItemController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("json_form:json_file",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must attach file!", "You must attach file!"));
            return;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        /* if (days < 1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Days to ripen/Accelerating growth in days must be greater than zero.", "Days to ripen/Accelerating growth in days must be greater than zero."));
            this.days = 1;
        }*/
        return days;
    }

    public void setDays(int days) {
        /*if (days < 1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Days to ripen/Accelerating growth in days must be greater than zero.", "Days to ripen/Accelerating growth in days must be greater than zero."));
            this.days = 1;
        }*/
        this.days = days;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getChosen_seedlings() {
        chosen_seedlings = new LinkedList<>();
        for (int i = 0; i < chosen_seedlings_id.size(); i++) {
            Item item = ItemModel.get_item_by_id(chosen_seedlings_id.get(i));
            chosen_seedlings.add(item);
        }
        return chosen_seedlings;
    }

    public void setChosen_seedlings(List<Item> chosen_seedlings) {
        this.chosen_seedlings = chosen_seedlings;
    }

    public List<Item> getSeedlings() {
        return seedlings;
    }

    public void setSeedlings(List<Item> seedlings) {
        this.seedlings = seedlings;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public UploadedFile getJson_file() {
        return json_file;
    }

    public void setJson_file(UploadedFile json_file) {
        this.json_file = json_file;
    }

    public boolean isAll_selected() {
        return all_selected;
    }

    public void setAll_selected(boolean all_selected) {
        this.all_selected = all_selected;
    }

    public List<Integer> getChosen_seedlings_id() {
        return chosen_seedlings_id;
    }

    public void setChosen_seedlings_id(List<Integer> chosen_seedlings_id) {
        this.chosen_seedlings_id = chosen_seedlings_id;
    }

}
