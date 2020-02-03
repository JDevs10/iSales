package com.iSales.database.entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by JDevs on 16/01/2020.
 */

@Entity(tableName = "virtual_product")
public class VirtualProductEntry {
    @PrimaryKey(autoGenerate = false)
    private int _0;// = 6478
    private int _1;// = 9718
    private int _2;// = 9717;
    private int _3;// = 80;
    private int _4;// = 1;
    private int _5;// = 9718;
    private int _6;// = 65016P;
    private int _7;// = 1;
    private String _8;// = "";
    private String _9;// = 2019-11-28 03:39:53;
    private String _10;// = 2019-11-28 16:42:42;
    private int _11;// = 0;
    private String _12;// = BESCUIT MAJOR EXTRA 175G PALETTE;
    private String _13;// = "";
    private String _14;// = "";
    private String _15;// = "";
    private String _16;// = "";
    private String _17;// = 10;
    private double _18;// = 944.00000000;
    private double _19;// = 995.92000000;
    private double _20;// = 0.00000000;
    private double _21;// = 0.00000000;
    private String _22;// = HT;
    private double _23;// = 0.00000000;
    private String _24;// = "";
    private double _25;// = 5.500;
    private int _26;// = 0;
    private double _27;// = 0.000;
    private int _28;// = 0;
    private double _29;// = 0.000;
    private int _30;// = 0;
    private int _31;// = 0;
    private int _32;// = 25;
    private int _33;// = 1;
    private int _34;// = 1;
    private int _35;// = 0;
    private int _36;// = 0;
    private int _37;// = 0;
    private String _38;// = "";
    private int _39;// = 0;
    private String _40;// = "";
    private String _41;// = "6194008534019P";
    private int _42;// = 2;
    private String _43;// = "";
    private String _44;// = "";
    private String _45;// = "";
    private String _46;// = "";
    private String _47;// = "";
    private int _48;// = 175;
    private int _49;// -3;
    private int _50;// = 0;
    private int _51;// = 0;
    private int _52;// = 0;
    private int _53;// = 0;
    private int _54;// = 0;
    private int _55;// = 0;
    private int _56;// = 0;
    private int _57;// = 0;
    private int _58;// = 0;
    private int _59;// = 0;
    private int _60;// = 0;
    private double _61;// = 0.00000000;
    private String _62;// = "";
    private String _63;// = "";
    private String _64;// = "";
    private String _65;// = "";
    private String _66;// = "";
    private int _67;// = 0;
    private String _68;// = "";
    private String _69;// = "";
    private String _70;// = "";
    private String _71;// = "";
    private String _72;// = "";
    private int _73;// = 0;
    private int rowid;// = 9718;
    private int fk_product_pere;// = 9718;
    private int fk_product_fils;// = 9717;
    private int qty;// = 80;
    private int incdec;// = 1;
    private String ref;// = "65016P";
    private int entity;// = 1;
    private String ref_ext;// = "";
    private String datec;// = "2019-11-28 03:39:53";
    private String tms;// = "2019-11-28 16:42:42";
    private int fk_parent;// = 0;
    private String label;// = "BESCUIT MAJOR EXTRA 175G PALETTE";
    private String description;// = "";
    private String note_public;// = "";
    private String note;// = "";
    private String customcode;// = "";
    private int fk_country;// = 10;
    private double price;// = 944.00000000;
    private double price_ttc;// = 995.92000000;
    private double price_min;// = 0.00000000;
    private double price_min_ttc;// = 0.00000000;
    private String price_base_type;// = "HT";
    private double cost_price;// = 0.00000000;
    private String default_vat_code;// = "";
    private double tva_tx;// = 5.500;
    private int recuperableonly;// = 0;
    private double localtax1_tx;// = 0.000;
    private int localtax1_type;// = 0;
    private double localtax2_tx;// = 0.000;
    private int localtax2_type;// = 0;
    private int fk_user_author;// = 0;
    private int fk_user_modif;// = 25;
    private int tosell;// = 1;
    private int tobuy;// = 1;
    private int onportal;// = 0;
    private int tobatch;// = 0;
    private int fk_product_type;// = 0;
    private String duration;// = "";
    private int seuil_stock_alerte;// = 0;
    private String url;// = "";
    private String barcode;// = 6194008534019P;
    private int fk_barcode_type;// = 2;
    private String accountancy_code_sell;// = "";
    private String accountancy_code_sell_intra;// = "";
    private String accountancy_code_sell_export;// = "";
    private String accountancy_code_buy;// = "";
    private String partnumber;// = "";
    private int weight;// = 175;
    private int weight_units;// = -3;
    private int length;// = 0;
    private int length_units;// = 0;
    private int width;// = 0;
    private int width_units;// = 0;
    private int height;// = 0;
    private int height_units;// = 0;
    private int surface;// = 0;
    private int surface_units;// = 0;
    private int volume;// = 0;
    private int volume_units;// = 0;
    private int stock;// = 0;
    private double pmp;// = 0.00000000;
    private String fifo;// = "";
    private String lifo;// = "";
    private String fk_default_warehouse;// = "";
    private String canvas;// = "";
    private String finished;// = "";
    private int hidden;// = 0;
    private String import_key;// = "";
    private String model_pdf;// = "";
    private String fk_price_expression;// = "";
    private String desiredstock;// = "";
    private String fk_unit;// = "";
    private int price_autogen;// = 0;

    public VirtualProductEntry(){

    }

    public VirtualProductEntry(int _0, int _1, int _2, int _3, int _4, int _5, int _6, int _7, String _8, String _9, String _10, int _11, String _12, String _13, String _14, String _15, String _16, String _17, double _18, double _19, double _20, double _21, String _22, double _23, String _24, double _25, int _26, double _27, int _28, double _29, int _30, int _31, int _32, int _33, int _34, int _35, int _36, int _37, String _38, int _39, String _40, String _41, int _42, String _43, String _44, String _45, String _46, String _47, int _48, int _49, int _50, int _51, int _52, int _53, int _54, int _55, int _56, int _57, int _58, int _59, int _60, double _61, String _62, String _63, String _64, String _65, String _66, int _67, String _68, String _69, String _70, String _71, String _72, int _73, int rowid, int fk_product_pere, int fk_product_fils, int qty, int incdec, String ref, int entity, String ref_ext, String datec, String tms, int fk_parent, String label, String description, String note_public, String note, String customcode, int fk_country, double price, double price_ttc, double price_min, double price_min_ttc, String price_base_type, double cost_price, String default_vat_code, double tva_tx, int recuperableonly, double localtax1_tx, int localtax1_type, double localtax2_tx, int localtax2_type, int fk_user_author, int fk_user_modif, int tosell, int tobuy, int onportal, int tobatch, int fk_product_type, String duration, int seuil_stock_alerte, String url, String barcode, int fk_barcode_type, String accountancy_code_sell, String accountancy_code_sell_intra, String accountancy_code_sell_export, String accountancy_code_buy, String partnumber, int weight, int weight_units, int length, int length_units, int width, int width_units, int height, int height_units, int surface, int surface_units, int volume, int volume_units, int stock, double pmp, String fifo, String lifo, String fk_default_warehouse, String canvas, String finished, int hidden, String import_key, String model_pdf, String fk_price_expression, String desiredstock, String fk_unit, int price_autogen) {
        this._0 = _0;
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
        this._7 = _7;
        this._8 = _8;
        this._9 = _9;
        this._10 = _10;
        this._11 = _11;
        this._12 = _12;
        this._13 = _13;
        this._14 = _14;
        this._15 = _15;
        this._16 = _16;
        this._17 = _17;
        this._18 = _18;
        this._19 = _19;
        this._20 = _20;
        this._21 = _21;
        this._22 = _22;
        this._23 = _23;
        this._24 = _24;
        this._25 = _25;
        this._26 = _26;
        this._27 = _27;
        this._28 = _28;
        this._29 = _29;
        this._30 = _30;
        this._31 = _31;
        this._32 = _32;
        this._33 = _33;
        this._34 = _34;
        this._35 = _35;
        this._36 = _36;
        this._37 = _37;
        this._38 = _38;
        this._39 = _39;
        this._40 = _40;
        this._41 = _41;
        this._42 = _42;
        this._43 = _43;
        this._44 = _44;
        this._45 = _45;
        this._46 = _46;
        this._47 = _47;
        this._48 = _48;
        this._49 = _49;
        this._50 = _50;
        this._51 = _51;
        this._52 = _52;
        this._53 = _53;
        this._54 = _54;
        this._55 = _55;
        this._56 = _56;
        this._57 = _57;
        this._58 = _58;
        this._59 = _59;
        this._60 = _60;
        this._61 = _61;
        this._62 = _62;
        this._63 = _63;
        this._64 = _64;
        this._65 = _65;
        this._66 = _66;
        this._67 = _67;
        this._68 = _68;
        this._69 = _69;
        this._70 = _70;
        this._71 = _71;
        this._72 = _72;
        this._73 = _73;
        this.rowid = rowid;
        this.fk_product_pere = fk_product_pere;
        this.fk_product_fils = fk_product_fils;
        this.qty = qty;
        this.incdec = incdec;
        this.ref = ref;
        this.entity = entity;
        this.ref_ext = ref_ext;
        this.datec = datec;
        this.tms = tms;
        this.fk_parent = fk_parent;
        this.label = label;
        this.description = description;
        this.note_public = note_public;
        this.note = note;
        this.customcode = customcode;
        this.fk_country = fk_country;
        this.price = price;
        this.price_ttc = price_ttc;
        this.price_min = price_min;
        this.price_min_ttc = price_min_ttc;
        this.price_base_type = price_base_type;
        this.cost_price = cost_price;
        this.default_vat_code = default_vat_code;
        this.tva_tx = tva_tx;
        this.recuperableonly = recuperableonly;
        this.localtax1_tx = localtax1_tx;
        this.localtax1_type = localtax1_type;
        this.localtax2_tx = localtax2_tx;
        this.localtax2_type = localtax2_type;
        this.fk_user_author = fk_user_author;
        this.fk_user_modif = fk_user_modif;
        this.tosell = tosell;
        this.tobuy = tobuy;
        this.onportal = onportal;
        this.tobatch = tobatch;
        this.fk_product_type = fk_product_type;
        this.duration = duration;
        this.seuil_stock_alerte = seuil_stock_alerte;
        this.url = url;
        this.barcode = barcode;
        this.fk_barcode_type = fk_barcode_type;
        this.accountancy_code_sell = accountancy_code_sell;
        this.accountancy_code_sell_intra = accountancy_code_sell_intra;
        this.accountancy_code_sell_export = accountancy_code_sell_export;
        this.accountancy_code_buy = accountancy_code_buy;
        this.partnumber = partnumber;
        this.weight = weight;
        this.weight_units = weight_units;
        this.length = length;
        this.length_units = length_units;
        this.width = width;
        this.width_units = width_units;
        this.height = height;
        this.height_units = height_units;
        this.surface = surface;
        this.surface_units = surface_units;
        this.volume = volume;
        this.volume_units = volume_units;
        this.stock = stock;
        this.pmp = pmp;
        this.fifo = fifo;
        this.lifo = lifo;
        this.fk_default_warehouse = fk_default_warehouse;
        this.canvas = canvas;
        this.finished = finished;
        this.hidden = hidden;
        this.import_key = import_key;
        this.model_pdf = model_pdf;
        this.fk_price_expression = fk_price_expression;
        this.desiredstock = desiredstock;
        this.fk_unit = fk_unit;
        this.price_autogen = price_autogen;
    }

    public int get_0() {
        return _0;
    }

    public void set_0(int _0) {
        this._0 = _0;
    }

    public int get_1() {
        return _1;
    }

    public void set_1(int _1) {
        this._1 = _1;
    }

    public int get_2() {
        return _2;
    }

    public void set_2(int _2) {
        this._2 = _2;
    }

    public int get_3() {
        return _3;
    }

    public void set_3(int _3) {
        this._3 = _3;
    }

    public int get_4() {
        return _4;
    }

    public void set_4(int _4) {
        this._4 = _4;
    }

    public int get_5() {
        return _5;
    }

    public void set_5(int _5) {
        this._5 = _5;
    }

    public int get_6() {
        return _6;
    }

    public void set_6(int _6) {
        this._6 = _6;
    }

    public int get_7() {
        return _7;
    }

    public void set_7(int _7) {
        this._7 = _7;
    }

    public String get_8() {
        return _8;
    }

    public void set_8(String _8) {
        this._8 = _8;
    }

    public String get_9() {
        return _9;
    }

    public void set_9(String _9) {
        this._9 = _9;
    }

    public String get_10() {
        return _10;
    }

    public void set_10(String _10) {
        this._10 = _10;
    }

    public int get_11() {
        return _11;
    }

    public void set_11(int _11) {
        this._11 = _11;
    }

    public String get_12() {
        return _12;
    }

    public void set_12(String _12) {
        this._12 = _12;
    }

    public String get_13() {
        return _13;
    }

    public void set_13(String _13) {
        this._13 = _13;
    }

    public String get_14() {
        return _14;
    }

    public void set_14(String _14) {
        this._14 = _14;
    }

    public String get_15() {
        return _15;
    }

    public void set_15(String _15) {
        this._15 = _15;
    }

    public String get_16() {
        return _16;
    }

    public void set_16(String _16) {
        this._16 = _16;
    }

    public String get_17() {
        return _17;
    }

    public void set_17(String _17) {
        this._17 = _17;
    }

    public double get_18() {
        return _18;
    }

    public void set_18(double _18) {
        this._18 = _18;
    }

    public double get_19() {
        return _19;
    }

    public void set_19(double _19) {
        this._19 = _19;
    }

    public double get_20() {
        return _20;
    }

    public void set_20(double _20) {
        this._20 = _20;
    }

    public double get_21() {
        return _21;
    }

    public void set_21(double _21) {
        this._21 = _21;
    }

    public String get_22() {
        return _22;
    }

    public void set_22(String _22) {
        this._22 = _22;
    }

    public double get_23() {
        return _23;
    }

    public void set_23(double _23) {
        this._23 = _23;
    }

    public String get_24() {
        return _24;
    }

    public void set_24(String _24) {
        this._24 = _24;
    }

    public double get_25() {
        return _25;
    }

    public void set_25(double _25) {
        this._25 = _25;
    }

    public int get_26() {
        return _26;
    }

    public void set_26(int _26) {
        this._26 = _26;
    }

    public double get_27() {
        return _27;
    }

    public void set_27(double _27) {
        this._27 = _27;
    }

    public int get_28() {
        return _28;
    }

    public void set_28(int _28) {
        this._28 = _28;
    }

    public double get_29() {
        return _29;
    }

    public void set_29(double _29) {
        this._29 = _29;
    }

    public int get_30() {
        return _30;
    }

    public void set_30(int _30) {
        this._30 = _30;
    }

    public int get_31() {
        return _31;
    }

    public void set_31(int _31) {
        this._31 = _31;
    }

    public int get_32() {
        return _32;
    }

    public void set_32(int _32) {
        this._32 = _32;
    }

    public int get_33() {
        return _33;
    }

    public void set_33(int _33) {
        this._33 = _33;
    }

    public int get_34() {
        return _34;
    }

    public void set_34(int _34) {
        this._34 = _34;
    }

    public int get_35() {
        return _35;
    }

    public void set_35(int _35) {
        this._35 = _35;
    }

    public int get_36() {
        return _36;
    }

    public void set_36(int _36) {
        this._36 = _36;
    }

    public int get_37() {
        return _37;
    }

    public void set_37(int _37) {
        this._37 = _37;
    }

    public String get_38() {
        return _38;
    }

    public void set_38(String _38) {
        this._38 = _38;
    }

    public int get_39() {
        return _39;
    }

    public void set_39(int _39) {
        this._39 = _39;
    }

    public String get_40() {
        return _40;
    }

    public void set_40(String _40) {
        this._40 = _40;
    }

    public String get_41() {
        return _41;
    }

    public void set_41(String _41) {
        this._41 = _41;
    }

    public int get_42() {
        return _42;
    }

    public void set_42(int _42) {
        this._42 = _42;
    }

    public String get_43() {
        return _43;
    }

    public void set_43(String _43) {
        this._43 = _43;
    }

    public String get_44() {
        return _44;
    }

    public void set_44(String _44) {
        this._44 = _44;
    }

    public String get_45() {
        return _45;
    }

    public void set_45(String _45) {
        this._45 = _45;
    }

    public String get_46() {
        return _46;
    }

    public void set_46(String _46) {
        this._46 = _46;
    }

    public String get_47() {
        return _47;
    }

    public void set_47(String _47) {
        this._47 = _47;
    }

    public int get_48() {
        return _48;
    }

    public void set_48(int _48) {
        this._48 = _48;
    }

    public int get_49() {
        return _49;
    }

    public void set_49(int _49) {
        this._49 = _49;
    }

    public int get_50() {
        return _50;
    }

    public void set_50(int _50) {
        this._50 = _50;
    }

    public int get_51() {
        return _51;
    }

    public void set_51(int _51) {
        this._51 = _51;
    }

    public int get_52() {
        return _52;
    }

    public void set_52(int _52) {
        this._52 = _52;
    }

    public int get_53() {
        return _53;
    }

    public void set_53(int _53) {
        this._53 = _53;
    }

    public int get_54() {
        return _54;
    }

    public void set_54(int _54) {
        this._54 = _54;
    }

    public int get_55() {
        return _55;
    }

    public void set_55(int _55) {
        this._55 = _55;
    }

    public int get_56() {
        return _56;
    }

    public void set_56(int _56) {
        this._56 = _56;
    }

    public int get_57() {
        return _57;
    }

    public void set_57(int _57) {
        this._57 = _57;
    }

    public int get_58() {
        return _58;
    }

    public void set_58(int _58) {
        this._58 = _58;
    }

    public int get_59() {
        return _59;
    }

    public void set_59(int _59) {
        this._59 = _59;
    }

    public int get_60() {
        return _60;
    }

    public void set_60(int _60) {
        this._60 = _60;
    }

    public double get_61() {
        return _61;
    }

    public void set_61(double _61) {
        this._61 = _61;
    }

    public String get_62() {
        return _62;
    }

    public void set_62(String _62) {
        this._62 = _62;
    }

    public String get_63() {
        return _63;
    }

    public void set_63(String _63) {
        this._63 = _63;
    }

    public String get_64() {
        return _64;
    }

    public void set_64(String _64) {
        this._64 = _64;
    }

    public String get_65() {
        return _65;
    }

    public void set_65(String _65) {
        this._65 = _65;
    }

    public String get_66() {
        return _66;
    }

    public void set_66(String _66) {
        this._66 = _66;
    }

    public int get_67() {
        return _67;
    }

    public void set_67(int _67) {
        this._67 = _67;
    }

    public String get_68() {
        return _68;
    }

    public void set_68(String _68) {
        this._68 = _68;
    }

    public String get_69() {
        return _69;
    }

    public void set_69(String _69) {
        this._69 = _69;
    }

    public String get_70() {
        return _70;
    }

    public void set_70(String _70) {
        this._70 = _70;
    }

    public String get_71() {
        return _71;
    }

    public void set_71(String _71) {
        this._71 = _71;
    }

    public String get_72() {
        return _72;
    }

    public void set_72(String _72) {
        this._72 = _72;
    }

    public int get_73() {
        return _73;
    }

    public void set_73(int _73) {
        this._73 = _73;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public int getFk_product_pere() {
        return fk_product_pere;
    }

    public void setFk_product_pere(int fk_product_pere) {
        this.fk_product_pere = fk_product_pere;
    }

    public int getFk_product_fils() {
        return fk_product_fils;
    }

    public void setFk_product_fils(int fk_product_fils) {
        this.fk_product_fils = fk_product_fils;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getIncdec() {
        return incdec;
    }

    public void setIncdec(int incdec) {
        this.incdec = incdec;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

    public String getRef_ext() {
        return ref_ext;
    }

    public void setRef_ext(String ref_ext) {
        this.ref_ext = ref_ext;
    }

    public String getDatec() {
        return datec;
    }

    public void setDatec(String datec) {
        this.datec = datec;
    }

    public String getTms() {
        return tms;
    }

    public void setTms(String tms) {
        this.tms = tms;
    }

    public int getFk_parent() {
        return fk_parent;
    }

    public void setFk_parent(int fk_parent) {
        this.fk_parent = fk_parent;
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

    public String getNote_public() {
        return note_public;
    }

    public void setNote_public(String note_public) {
        this.note_public = note_public;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCustomcode() {
        return customcode;
    }

    public void setCustomcode(String customcode) {
        this.customcode = customcode;
    }

    public int getFk_country() {
        return fk_country;
    }

    public void setFk_country(int fk_country) {
        this.fk_country = fk_country;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_ttc() {
        return price_ttc;
    }

    public void setPrice_ttc(double price_ttc) {
        this.price_ttc = price_ttc;
    }

    public double getPrice_min() {
        return price_min;
    }

    public void setPrice_min(double price_min) {
        this.price_min = price_min;
    }

    public double getPrice_min_ttc() {
        return price_min_ttc;
    }

    public void setPrice_min_ttc(double price_min_ttc) {
        this.price_min_ttc = price_min_ttc;
    }

    public String getPrice_base_type() {
        return price_base_type;
    }

    public void setPrice_base_type(String price_base_type) {
        this.price_base_type = price_base_type;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public String getDefault_vat_code() {
        return default_vat_code;
    }

    public void setDefault_vat_code(String default_vat_code) {
        this.default_vat_code = default_vat_code;
    }

    public double getTva_tx() {
        return tva_tx;
    }

    public void setTva_tx(double tva_tx) {
        this.tva_tx = tva_tx;
    }

    public int getRecuperableonly() {
        return recuperableonly;
    }

    public void setRecuperableonly(int recuperableonly) {
        this.recuperableonly = recuperableonly;
    }

    public double getLocaltax1_tx() {
        return localtax1_tx;
    }

    public void setLocaltax1_tx(double localtax1_tx) {
        this.localtax1_tx = localtax1_tx;
    }

    public int getLocaltax1_type() {
        return localtax1_type;
    }

    public void setLocaltax1_type(int localtax1_type) {
        this.localtax1_type = localtax1_type;
    }

    public double getLocaltax2_tx() {
        return localtax2_tx;
    }

    public void setLocaltax2_tx(double localtax2_tx) {
        this.localtax2_tx = localtax2_tx;
    }

    public int getLocaltax2_type() {
        return localtax2_type;
    }

    public void setLocaltax2_type(int localtax2_type) {
        this.localtax2_type = localtax2_type;
    }

    public int getFk_user_author() {
        return fk_user_author;
    }

    public void setFk_user_author(int fk_user_author) {
        this.fk_user_author = fk_user_author;
    }

    public int getFk_user_modif() {
        return fk_user_modif;
    }

    public void setFk_user_modif(int fk_user_modif) {
        this.fk_user_modif = fk_user_modif;
    }

    public int getTosell() {
        return tosell;
    }

    public void setTosell(int tosell) {
        this.tosell = tosell;
    }

    public int getTobuy() {
        return tobuy;
    }

    public void setTobuy(int tobuy) {
        this.tobuy = tobuy;
    }

    public int getOnportal() {
        return onportal;
    }

    public void setOnportal(int onportal) {
        this.onportal = onportal;
    }

    public int getTobatch() {
        return tobatch;
    }

    public void setTobatch(int tobatch) {
        this.tobatch = tobatch;
    }

    public int getFk_product_type() {
        return fk_product_type;
    }

    public void setFk_product_type(int fk_product_type) {
        this.fk_product_type = fk_product_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getSeuil_stock_alerte() {
        return seuil_stock_alerte;
    }

    public void setSeuil_stock_alerte(int seuil_stock_alerte) {
        this.seuil_stock_alerte = seuil_stock_alerte;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getFk_barcode_type() {
        return fk_barcode_type;
    }

    public void setFk_barcode_type(int fk_barcode_type) {
        this.fk_barcode_type = fk_barcode_type;
    }

    public String getAccountancy_code_sell() {
        return accountancy_code_sell;
    }

    public void setAccountancy_code_sell(String accountancy_code_sell) {
        this.accountancy_code_sell = accountancy_code_sell;
    }

    public String getAccountancy_code_sell_intra() {
        return accountancy_code_sell_intra;
    }

    public void setAccountancy_code_sell_intra(String accountancy_code_sell_intra) {
        this.accountancy_code_sell_intra = accountancy_code_sell_intra;
    }

    public String getAccountancy_code_sell_export() {
        return accountancy_code_sell_export;
    }

    public void setAccountancy_code_sell_export(String accountancy_code_sell_export) {
        this.accountancy_code_sell_export = accountancy_code_sell_export;
    }

    public String getAccountancy_code_buy() {
        return accountancy_code_buy;
    }

    public void setAccountancy_code_buy(String accountancy_code_buy) {
        this.accountancy_code_buy = accountancy_code_buy;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight_units() {
        return weight_units;
    }

    public void setWeight_units(int weight_units) {
        this.weight_units = weight_units;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength_units() {
        return length_units;
    }

    public void setLength_units(int length_units) {
        this.length_units = length_units;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth_units() {
        return width_units;
    }

    public void setWidth_units(int width_units) {
        this.width_units = width_units;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight_units() {
        return height_units;
    }

    public void setHeight_units(int height_units) {
        this.height_units = height_units;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getSurface_units() {
        return surface_units;
    }

    public void setSurface_units(int surface_units) {
        this.surface_units = surface_units;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume_units() {
        return volume_units;
    }

    public void setVolume_units(int volume_units) {
        this.volume_units = volume_units;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPmp() {
        return pmp;
    }

    public void setPmp(double pmp) {
        this.pmp = pmp;
    }

    public String getFifo() {
        return fifo;
    }

    public void setFifo(String fifo) {
        this.fifo = fifo;
    }

    public String getLifo() {
        return lifo;
    }

    public void setLifo(String lifo) {
        this.lifo = lifo;
    }

    public String getFk_default_warehouse() {
        return fk_default_warehouse;
    }

    public void setFk_default_warehouse(String fk_default_warehouse) {
        this.fk_default_warehouse = fk_default_warehouse;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public String getImport_key() {
        return import_key;
    }

    public void setImport_key(String import_key) {
        this.import_key = import_key;
    }

    public String getModel_pdf() {
        return model_pdf;
    }

    public void setModel_pdf(String model_pdf) {
        this.model_pdf = model_pdf;
    }

    public String getFk_price_expression() {
        return fk_price_expression;
    }

    public void setFk_price_expression(String fk_price_expression) {
        this.fk_price_expression = fk_price_expression;
    }

    public String getDesiredstock() {
        return desiredstock;
    }

    public void setDesiredstock(String desiredstock) {
        this.desiredstock = desiredstock;
    }

    public String getFk_unit() {
        return fk_unit;
    }

    public void setFk_unit(String fk_unit) {
        this.fk_unit = fk_unit;
    }

    public int getPrice_autogen() {
        return price_autogen;
    }

    public void setPrice_autogen(int price_autogen) {
        this.price_autogen = price_autogen;
    }
}
