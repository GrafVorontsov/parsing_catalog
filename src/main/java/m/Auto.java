package m;

import java.util.ArrayList;

public class Auto implements Comparable{
    private int marka_id;
    private int model_id;
    private int car_id;
    private String marka_name;
    private String model_name;
    private String car_name;
    private String correction;
    private String year;
    private String range_type;
    private String install;
    private String art_number;
    private ArrayList<String> info;
    private ArrayList<String> info_lowering;
    private String jpg;
    private ArrayList<String> pdf;

    public Auto(int marka_id, int model_id, int car_id, String marka_name, String model_name, String car_name, String correction, String year, String range_type, String install, String art_number, ArrayList<String> info, ArrayList<String> info_lowering, String jpg, ArrayList<String> pdf) {
        this.marka_id = marka_id;
        this.model_id = model_id;
        this.car_id = car_id;
        this.marka_name = marka_name;
        this.model_name = model_name;
        this.car_name = car_name;
        this.correction = correction;
        this.year = year;
        this.range_type = range_type;
        this.install = install;
        this.art_number = art_number;
        this.info = info;
        this.info_lowering = info_lowering;
        this.jpg = jpg;
        this.pdf = pdf;
    }

    public ArrayList<String> getInfo_lowering() {
        return info_lowering;
    }

    public void setInfo_lowering(ArrayList<String> info_lowering) {
        this.info_lowering = info_lowering;
    }

    public ArrayList<String> getPdf() {
        return pdf;
    }

    public void setPdf(ArrayList<String> pdf) {
        this.pdf = pdf;
    }

    public int getMarka_id() {
        return marka_id;
    }

    public void setMarka_id(int marka_id) {
        this.marka_id = marka_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getMarka_name() {
        return marka_name;
    }

    public void setMarka_name(String marka_name) {
        this.marka_name = marka_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRange_type() {
        return range_type;
    }

    public void setRange_type(String range_type) {
        this.range_type = range_type;
    }

    public String getInstall() {
        return install;
    }

    public void setInstall(String install) {
        this.install = install;
    }

    public String getArt_number() {
        return art_number;
    }

    public void setArt_number(String art_number) {
        this.art_number = art_number;
    }

    public ArrayList<String> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<String> info) {
        this.info = info;
    }

    public String getJpg() {
        return jpg;
    }

    public void setJpg(String jpg) {
        this.jpg = jpg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auto)) return false;

        Auto auto = (Auto) o;

        if (marka_id != auto.marka_id) return false;
        if (model_id != auto.model_id) return false;
        if (car_id != auto.car_id) return false;
        if (!marka_name.equals(auto.marka_name)) return false;
        if (!model_name.equals(auto.model_name)) return false;
        if (car_name != null ? !car_name.equals(auto.car_name) : auto.car_name != null) return false;
        if (correction != null ? !correction.equals(auto.correction) : auto.correction != null) return false;
        if (!year.equals(auto.year)) return false;
        if (!range_type.equals(auto.range_type)) return false;
        if (!install.equals(auto.install)) return false;
        if (!art_number.equals(auto.art_number)) return false;
        if (info != null ? !info.equals(auto.info) : auto.info != null) return false;
        return !(jpg != null ? !jpg.equals(auto.jpg) : auto.jpg != null);

    }

    @Override
    public int hashCode() {
        int result = marka_id;
        result = 31 * result + model_id;
        result = 31 * result + car_id;
        result = 31 * result + marka_name.hashCode();
        result = 31 * result + model_name.hashCode();
        result = 31 * result + (car_name != null ? car_name.hashCode() : 0);
        result = 31 * result + (correction != null ? correction.hashCode() : 0);
        result = 31 * result + year.hashCode();
        result = 31 * result + range_type.hashCode();
        result = 31 * result + install.hashCode();
        result = 31 * result + art_number.hashCode();
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (jpg != null ? jpg.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
