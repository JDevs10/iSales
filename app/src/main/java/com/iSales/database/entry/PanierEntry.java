package com.iSales.database.entry;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by netserve on 24/09/2018.
 */

@Entity(tableName = "panier")
public class PanierEntry {
    @PrimaryKey(autoGenerate = true)
    private Long panier_id;
    private Long id;
    private String label;
    private String description;
    private String price;
    private String price_ttc;
    private String price_min;
    private String price_min_ttc;
    private String price_base_type;
    private String tva_tx;
    private Integer stock_reel;
    private String stock_theorique;
    private String seuil_stock_alerte;
    private Boolean duration_value;
    private String duration_unit;
    private String weight;
    private String weight_units;
    private String length;
    private String length_units;
    private String surface;
    private String surface_units;
    private String volume;
    private long fk_product;
    private String volume_units;
    private String date_creation;
    private String date_modification;
    private String width;
    private String width_units;
    private String height;
    private String height_units;
    private String file_content;
    private String ref;
    private String remise;
    private String remise_percent;
    private int quantity;
    private String poster_content;

    public PanierEntry() {
    }

    @Ignore
    public PanierEntry(String label, String price, String price_ttc, int quantity) {
        this.label = label;
        this.price = price;
        this.price_ttc = price_ttc;
        this.quantity = quantity;
    }

    public Long getPanier_id() {
        return panier_id;
    }

    public void setPanier_id(Long panier_id) {
        this.panier_id = panier_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_ttc() {
        return price_ttc;
    }

    public void setPrice_ttc(String price_ttc) {
        this.price_ttc = price_ttc;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_min_ttc() {
        return price_min_ttc;
    }

    public void setPrice_min_ttc(String price_min_ttc) {
        this.price_min_ttc = price_min_ttc;
    }

    public String getPrice_base_type() {
        return price_base_type;
    }

    public void setPrice_base_type(String price_base_type) {
        this.price_base_type = price_base_type;
    }

    public String getTva_tx() {
        return tva_tx;
    }

    public void setTva_tx(String tva_tx) {
        this.tva_tx = tva_tx;
    }

    public Integer getStock_reel() {
        return stock_reel;
    }

    public void setStock_reel(Integer stock_reel) {
        this.stock_reel = stock_reel;
    }

    public String getStock_theorique() {
        return stock_theorique;
    }

    public void setStock_theorique(String stock_theorique) {
        this.stock_theorique = stock_theorique;
    }

    public String getSeuil_stock_alerte() {
        return seuil_stock_alerte;
    }

    public void setSeuil_stock_alerte(String seuil_stock_alerte) {
        this.seuil_stock_alerte = seuil_stock_alerte;
    }

    public Boolean getDuration_value() {
        return duration_value;
    }

    public void setDuration_value(Boolean duration_value) {
        this.duration_value = duration_value;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public void setDuration_unit(String duration_unit) {
        this.duration_unit = duration_unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_units() {
        return weight_units;
    }

    public void setWeight_units(String weight_units) {
        this.weight_units = weight_units;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength_units() {
        return length_units;
    }

    public void setLength_units(String length_units) {
        this.length_units = length_units;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getSurface_units() {
        return surface_units;
    }

    public void setSurface_units(String surface_units) {
        this.surface_units = surface_units;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolume_units() {
        return volume_units;
    }

    public void setVolume_units(String volume_units) {
        this.volume_units = volume_units;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getDate_modification() {
        return date_modification;
    }

    public void setDate_modification(String date_modification) {
        this.date_modification = date_modification;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth_units() {
        return width_units;
    }

    public void setWidth_units(String width_units) {
        this.width_units = width_units;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight_units() {
        return height_units;
    }

    public void setHeight_units(String height_units) {
        this.height_units = height_units;
    }

    public String getFile_content() {
        return file_content;
    }

    public void setFile_content(String file_content) {
        this.file_content = file_content;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPoster_content() {
        return poster_content;
    }

    public void setPoster_content(String poster_content) {
        this.poster_content = poster_content;
    }

    public long getFk_product() {
        return fk_product;
    }

    public void setFk_product(long fk_product) {
        this.fk_product = fk_product;
    }

    public String getRemise() {
        return remise;
    }

    public void setRemise(String remise) {
        this.remise = remise;
    }

    public String getRemise_percent() {
        return remise_percent;
    }

    public void setRemise_percent(String remise_percent) {
        this.remise_percent = remise_percent;
    }
}
