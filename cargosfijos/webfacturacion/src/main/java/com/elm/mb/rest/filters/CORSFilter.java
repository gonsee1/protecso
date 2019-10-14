package com.elm.mb.rest.filters;
 
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
 
@Controller
public class CORSFilter /* extends OncePerRequestFilter */{
    private static final Log LOG = LogFactory.getLog(CORSFilter.class);
 
    @RequestMapping(value="/crossdomain",method = {RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> crossdomain(HttpServletRequest request, HttpServletResponse response){
    	Map<String,Object> map=new TreeMap<String, Object>();
    	map.put("clientes", null);    	
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,x-requested-with");
    	return map;
    }

}