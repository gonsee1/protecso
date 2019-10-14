/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var settings=(function(){
    var saveSettings=fnc.clone(settings);
    settings.real={//datos reales sin modificaciones
        get:function(){return saveSettings},
        icons:{
            getAll:function(){return saveSettings.ICONS_SYS},
            getUriImage:function(name){
                var r=saveSettings.ICONS_SYS[name];
                if (r && r.uri)
                    return r.uri;
                return fnc.images.uri.win_modulo_default();
            },
            getCssImage:function(name){
                var r=saveSettings.ICONS_SYS[name];
                if (r && r.cssName)
                    return r.cssName;
                return fnc.icons.class.win_modulo_default();
            }
        }
    };
    return settings;
})();