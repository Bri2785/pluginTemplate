package com.evnt.client.modules.so;

import com.evnt.util.Money;
import com.evnt.util.Quantity;

import com.fbi.fbo.impl.ApiCallType;
import com.fbi.fbo.impl.dataexport.QueryRow;

import com.fbi.fbo.impl.message.request.salesorder.AddSOItemRequestImpl;
import com.fbi.fbo.impl.orders.SalesOrderItemImpl;

import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.message.request.salesorder.AddSOItemRequest;
import com.fbi.fbo.message.response.ResponseBase;
import com.fbi.fbo.orders.SalesOrderItem;
import com.fbi.gui.util.UtilGui;
import com.fbi.plugins.FishbowlPluginButton;
import com.fbi.sdk.constants.SOItemStatusConst;
import com.fbi.sdk.constants.SOItemTypeConst;
import com.fbi.sdk.constants.UOMConst;
import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import com.unigrative.pluginUniquePackage.plugins.exception.FishbowlException;
import com.unigrative.pluginUniquePackage.plugins.fbapi.Api;
import com.unigrative.pluginUniquePackage.plugins.fbapi.ApiCaller;
import com.unigrative.pluginUniquePackage.plugins.repository.Repository;
import com.unigrative.pluginUniquePackage.plugins.util.property.PropertyGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;


public class AddSoItemUsingApiButton extends FishbowlPluginButton implements ApiCaller, PropertyGetter, Repository.RunSql
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AddSoItemUsingApiButton.class);;
    private Repository repository;
    private int soId;

    private QueryRow orderInfo; //QueryRow is what Fishbowl uses to hold returned sql data from the repository. The fields are access by column name


    public AddSoItemUsingApiButton() {
        this.repository = new Repository(this);
        this.setModuleName("Sales Order");
        this.setPluginName(GenericPlugin.MODULE_NAME);
        this.setIcon(new ImageIcon(this.getClass().getResource("/icon24/textanddocuments/documents/document_new.png")));
        this.setText("Add Item");

        this.addActionListener((event) -> {
            this.addItemActionPerformed();
        });
    }


    private void addItemActionPerformed() {
        try {
            if (!this.hasAccess("Add Item Button")) { //The Generic Plugin class creates the access rights that you want (around line 71). This is optional

                UtilGui.showMessageDialog("You do not have rights to access this feature.", "Access Denied", 1);
            } else {

                this.soId = this.getObjectId();
                if (this.soId < 0) {
                    UtilGui.showMessageDialog("There is no open order", "Open an order first", 1); //UtilGui is Fishbowl's wrapper for the majority of popup message communications
                } else {
                    addItemToSalesOrder();
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error: ", e);
        }
    }


    void addItemToSalesOrder() {
        LOGGER.debug("Add Item Method Called"); //Will output to log and cmd

        if (this.soId <= 0) {
            return;
        }

        this.orderInfo = this.repository.getOrderInfo(this.soId);

        final SalesOrderItem newItem = new SalesOrderItemImpl();
        newItem.setProductNumber("YOUR PRODUCT NUMBER HERE"); // If using special types (Ie Tax) it might not have a number. Check what it looks like on the SO module when adding a normal line first
        newItem.setDescription("FREE FORM DESCRIPTION"); //If you want to show the product's saved secription, you'll have to fetch it manually. This call doesnt populate it automatically
        newItem.setQuantity(Quantity.ONE); //You can use  new Quantity(7) for quantities with out a constant. Quantity behaves like a decimal field
        newItem.setItemType(SOItemTypeConst.SALE.getId()); //check the SOItemTypeConst calls for all options
        newItem.setStatus(SOItemStatusConst.ENTERED.getId()); //check SOItemStatusConst for all options

        Money itemPrice = new Money(200.00); //Money also behaves like a decimal

        newItem.setProductPrice(itemPrice);
        newItem.setTotalPrice(itemPrice.multiply(newItem.getQuantity()));

        newItem.setTaxable(false); //boolean

        newItem.setUOMCode(UOMConst.EACH.getCode().toLowerCase());

        newItem.setDateScheduledFulfillment(this.orderInfo.getDate("dateFirstShip"));
        newItem.setNewItemFlag(Boolean.TRUE);
        newItem.setTaxCode("NON");
        newItem.setQBClass(this.orderInfo.getString("qbClassName"));


        final AddSOItemRequest addSOItemRequest = new AddSOItemRequestImpl();
        addSOItemRequest.setOrderNum(this.orderInfo.getString("num"));
        addSOItemRequest.setSOItem(newItem);
        try {
            Api.ADD_SO_ITEM.call(this, addSOItemRequest);
        }
        catch (FishbowlException e) {
            UtilGui.showMessageDialog("Unable to add item to the sales order.", "Order Item Error", 0);
            LOGGER.error("Error adding item", e);
        }
        this.reloadObject(); // reloads the sales order
    }


    public ResponseBase call(final ApiCallType requestType, final RequestBase requestBase) throws FishbowlException {
        try {
            return this.runApiRequest(requestType, requestBase);
        }
        catch (Exception e) {
            throw new FishbowlException(e);
        }
    }

    public String getProperty(final String key) {
        return this.repository.getProperty(key);
    }

    public List<QueryRow> executeSql(final String query) {
        return this.runQuery(query);
    }

}

