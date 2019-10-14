/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

/**
 *
 * @author crodas
 */
public class Pagination {
    public Integer Page;
    public Integer Limit;

    public Integer getPage() {
        return Page;
    }

    public Integer getLimit() {
        return Limit;
    }

    public Integer getGroup(){
        return (this.Page-1)*this.Limit;
    }
    public void setPage(Integer Page) {        
        this.Page = Page;
        //this.calcGroup();
    }

    public void setLimit(Integer Limit) {        
        this.Limit = Limit;
        //this.calcGroup();
    }    
    
    
}
