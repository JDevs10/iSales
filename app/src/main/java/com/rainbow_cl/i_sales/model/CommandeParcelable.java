package com.rainbow_cl.i_sales.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.rainbow_cl.i_sales.remote.model.DolPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by netserve on 15/09/2018.
 */

public class CommandeParcelable implements Parcelable {
    private String ref;
    private String total;
    private long id;
    private long commande_id;
    private long socid;
    private long date;
    private long date_commande;
    private long date_livraison;
    private int statut;
    private int is_synchro;

    private ClientParcelable client;
    private ArrayList<ProduitParcelable> produits;

    public CommandeParcelable() {
    }

    public long getSocid() {
        return socid;
    }

    public void setSocid(long socid) {
        this.socid = socid;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate_commande() {
        return date_commande;
    }

    public void setDate_commande(long date_commande) {
        this.date_commande = date_commande;
    }

    public long getDate_livraison() {
        return date_livraison;
    }

    public void setDate_livraison(long date_livraison) {
        this.date_livraison = date_livraison;
    }

    public ClientParcelable getClient() {
        return client;
    }

    public void setClient(ClientParcelable client) {
        this.client = client;
    }

    public List<ProduitParcelable> getProduits() {
        return produits;
    }

    public void setProduits(ArrayList<ProduitParcelable> produits) {
        this.produits = produits;
    }

    public long getCommande_id() {
        return commande_id;
    }

    public void setCommande_id(long commande_id) {
        this.commande_id = commande_id;
    }

    public int getIs_synchro() {
        return is_synchro;
    }

    public void setIs_synchro(int is_synchro) {
        this.is_synchro = is_synchro;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ref);
        dest.writeString(this.total);
        dest.writeLong(this.id);
        dest.writeLong(this.commande_id);
        dest.writeLong(this.socid);
        dest.writeLong(this.date);
        dest.writeLong(this.date_commande);
        dest.writeLong(this.date_livraison);
        dest.writeInt(this.statut);
        dest.writeInt(this.is_synchro);
        dest.writeParcelable(this.client, flags);
        dest.writeTypedList(this.produits);
    }

    protected CommandeParcelable(Parcel in) {
        this.ref = in.readString();
        this.total = in.readString();
        this.id = in.readLong();
        this.commande_id = in.readLong();
        this.socid = in.readLong();
        this.date = in.readLong();
        this.date_commande = in.readLong();
        this.date_livraison = in.readLong();
        this.statut = in.readInt();
        this.is_synchro = in.readInt();
        this.client = in.readParcelable(ClientParcelable.class.getClassLoader());
        this.produits = in.createTypedArrayList(ProduitParcelable.CREATOR);
    }

    public static final Creator<CommandeParcelable> CREATOR = new Creator<CommandeParcelable>() {
        @Override
        public CommandeParcelable createFromParcel(Parcel source) {
            return new CommandeParcelable(source);
        }

        @Override
        public CommandeParcelable[] newArray(int size) {
            return new CommandeParcelable[size];
        }
    };
}