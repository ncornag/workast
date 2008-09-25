
var aMinuteInMillis = 60 * 1000;
var anHourInMillis = aMinuteInMillis * 60;
var aDayInMillis = anHourInMillis * 24;

// Extend jQuery
$.extend({
    // Utilities
    isArray: function(v){
        return typeof(v) == 'object' &&
                            v != null &&
                            typeof(v.length) == 'number';
    },
    isNull: function(o) {
        if(o == null && o == undefined) {
            return true;
        }
        return false;
    },
    exists: function(o) {
        return $(o).length>0;
    },
    modal: function(content, title) {
        title = title||'';
        if (!$.exists('#ui-dialog')) {
            dialog = document.createElement('div');
            $("<div id='ui-dialog'></div>").appendTo("body");
        }
        $('#ui-dialog')
            .html(content)
            .dialog({ 
                buttons: { 
                    "Ok": function() { 
                        $(this)
                            .dialog("close")
                            .html('');
                    } 
                }, 
                title: title,
                modal: true,
                autoOpen: false
            });
        $('#ui-dialog').dialog("open");
    },
    tmpl : function(tmpl, vals) {
        // Usage: $.tmpl('<div class="{classname}">{content}</div>', { 'classname' : 'my-class', 'content' : 'My content.' });
        var rgxp, repr;
        // default to doing no harm
        tmpl = tmpl   || '';
        vals = vals || {};
        // regular expression for matching our placeholders; e.g., #{my-cLaSs_name77}
        rgxp = /\{([^{}]*)}/g;
        // function to making replacements
        repr = function (str, match) {
            return typeof vals[match] === 'string' || typeof vals[match] === 'number' ? vals[match] : str;
        };
        return tmpl.replace(rgxp, repr);
    }
});

$.fn.prettyDate = function(){
	var now = new Date();
	return this.each(function() {
		var thisDate = fromISOString($(this).attr('timeData'));
		var prettyDate = thisDate.pretty(now);
		if ( prettyDate ) {
			$(this).text( prettyDate );
		}
	});
};

$.fn.validatorAjaxForm = function(submitOptions, validateOptions) {
    if (this.length>0) {
        validateOptions.onSubmit = false;
        var validator = this.validate(validateOptions);
        //validator.resetForm();
        var form;
        
        submitOptions.dataType = submitOptions.dataType||'json';
        submitOptions.beforeSubmit = function(formData, jQForm, options) {
            if (submitOptions.beforeSubmitCallback && submitOptions.beforeSubmitCallback(formData, jQForm, options)==false) return false;
            form = jQForm;
            
            // Eliminate null references (beforeSubmit could null some dates)
            for(var i = 0; i < formData.length; i++) {
                if (formData[i]==null) {
                    for(var j = i; j < formData.length-1; j++) {
                        formData[j] = formData[j+1]; 
                    }
                    formData.pop();
                }
            }
            
            return jQForm.valid();
        }
        submitOptions.success = function(json) {
            var errors = json.response.errors;
            var fieldErrors = json.response.fieldErrors;
            if (errors.length>0 || fieldErrors.length>0) {
                $.each(fieldErrors, function(i, val) {
                    var error = {};
                    error[val.key] = val.value;
                    validator.showErrors(error);
                });                        
                showResponseErrors(errors);
                //validator.resetForm();
            } else {
                submitOptions.successCallback(json, form);
            }
        }
        this.ajaxForm(submitOptions);
    }
}

if(jQuery.validator) {
    jQuery.validator.addMethod("nowhitespace", function(value, element) {
        return this.optional(element) || /^\S+$/i.test(value);
    }, "No white space please"); 
    
    jQuery.validator.addMethod("lettersonly", function(value, element) {
        return this.optional(element) || /^[a-z0-9]+$/i.test(value);
    }, "Invalid tag"); 
}
// Extend String
String.format = function(format){
    var A = Array.prototype.slice.call(arguments, 1);
    return format.replace(/\{(\d+)\}/g,  function(C,D){return A[D]});
};

// Extend Date
Date.prototype.clone = function() {
    return new Date(this.getTime());
};

Date.prototype.clearTime = function(clone){
    if(clone){
        return this.clone().clearTime();
    }
    this.setHours(0);
    this.setMinutes(0);
    this.setSeconds(0);
    this.setMilliseconds(0);
    return this;
};

Date.prototype.pretty = function(reference) {
    var referenceTime = reference.valueOf();
    var referenceDate = reference.clearTime(true).valueOf();
    var dtTime = this.valueOf();
    var dtDate = this.clearTime(true).valueOf();
    var formattedDate = '';
    if (referenceDate == dtDate) {
        if ((referenceTime - dtTime) >= anHourInMillis) {
            if (this.getHours()==1)
                formattedTime = this.format('todayat1');
            else 
                formattedTime = this.format('todayat');
        } else if((referenceTime - dtTime) >= aMinuteInMillis) {
            minutes = Math.floor((referenceTime - dtTime) / aMinuteInMillis);
            if (minutes==1) {
                formattedTime = this.format('minuteago');
            } else {
                formattedTime = this.format('minutesago', minutes);
            }
        } else {
            seconds = Math.floor((referenceTime - dtTime) / 1000);
            if (seconds<=2) {
                formattedTime = this.format('secondago');
            } else {
                formattedTime = this.format('secondsago', seconds);
            }
        }
    } else if((referenceDate - dtDate) == aDayInMillis) {
        if (this.getHours()==1)
            formattedTime = this.format('yesterdayat1');
        else
            formattedTime = this.format('yesterdayat');
    } else {
        if (this.getHours()==1)
            formattedTime = this.format('full1');
        else
            formattedTime = this.format('full');
    }
    return formattedTime;
}

Date.prototype.format = function(format, args) {
    if (Date.formats[format]) {
        format = String.format(Date.formats[format], args||{});
    }
    var inLiterals = false;
    var returnStr = '';
    var replace = Date.replaceChars;
    for (var i = 0; i < format.length; i++) {
        var curChar = format.charAt(i);
        if(curChar=="'")
            inLiterals = !inLiterals;
        if (!inLiterals && replace[curChar])
            returnStr += replace[curChar].call(this);
        else
            if (curChar != "'")
                returnStr += curChar;
    }
    return returnStr;
};

Date.formats = {
    secondago: "'one second ago'",
    secondsago: "'{0} seconds ago'",
    minuteago: "'one minute ago'",
    minutesago: "'{0} minutes ago'",
    todayat1: "'Today at' g':'i a",
    todayat: "'Today at' g':'i a",
    yesterdayat1: "'Yesterday at' g':'i a",
    yesterdayat: "'Yesterday at' g':'i a",
    full1: "F j',' Y',' g':'i a",
    full: "F j',' Y',' g':'i a",
    dataEntry: "m/d/Y"
};

Date.replaceChars = {
    shortMonths: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    longMonths: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
    shortDays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    longDays: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
    
    // Day
    d: function() { return (this.getDate() < 10 ? '0' : '') + this.getDate(); },
    D: function() { return Date.replaceChars.shortDays[this.getDay()]; },
    j: function() { return this.getDate(); },
    l: function() { return Date.replaceChars.longDays[this.getDay()]; },
    N: function() { return this.getDay() + 1; },
    S: function() { return (this.getDate() % 10 == 1 && this.getDate() != 11 ? 'st' : (this.getDate() % 10 == 2 && this.getDate() != 12 ? 'nd' : (this.getDate() % 10 == 3 && this.getDate() != 13 ? 'rd' : 'th'))); },
    w: function() { return this.getDay(); },
    z: function() { return "Not Yet Supported"; },
    // Week
    W: function() { return "Not Yet Supported"; },
    // Month
    F: function() { return Date.replaceChars.longMonths[this.getMonth()]; },
    m: function() { return (this.getMonth() < 11 ? '0' : '') + (this.getMonth() + 1); },
    M: function() { return Date.replaceChars.shortMonths[this.getMonth()]; },
    n: function() { return this.getMonth() + 1; },
    t: function() { return "Not Yet Supported"; },
    // Year
    L: function() { return "Not Yet Supported"; },
    o: function() { return "Not Supported"; },
    Y: function() { return this.getFullYear(); },
    y: function() { return ('' + this.getFullYear()).substr(2); },
    // Time
    a: function() { return this.getHours() < 12 ? 'am' : 'pm'; },
    A: function() { return this.getHours() < 12 ? 'AM' : 'PM'; },
    B: function() { return "Not Yet Supported"; },
    g: function() { return this.getHours() == 0 ? 12 : (this.getHours() > 12 ? this.getHours() - 12 : this.getHours()); },
    G: function() { return this.getHours(); },
    h: function() { return (this.getHours() < 10 || (12 < this.getHours() < 22) ? '0' : '') + (this.getHours() < 10 ? this.getHours() + 1 : this.getHours() - 12); },
    H: function() { return (this.getHours() < 10 ? '0' : '') + this.getHours(); },
    i: function() { return (this.getMinutes() < 10 ? '0' : '') + this.getMinutes(); },
    s: function() { return (this.getSeconds() < 10 ? '0' : '') + this.getSeconds(); },
    // Timezone
    e: function() { return "Not Yet Supported"; },
    I: function() { return "Not Supported"; },
    O: function() { return (this.getTimezoneOffset() < 0 ? '-' : '+') + (this.getTimezoneOffset() / 60 < 10 ? '0' : '') + (this.getTimezoneOffset() / 60) + '00'; },
    T: function() { return "Not Yet Supported"; },
    Z: function() { return this.getTimezoneOffset() * 60; },
    // Full Date/Time
    c: function() { return "Not Yet Supported"; },
    r: function() { return this.toString(); },
    U: function() { return this.getTime() / 1000; }
}

// Show Erros
function showResponseErrors(errors) {
    errors = [].concat(errors||[]);
    var htmlErrors = '';
    $.each(errors, function(i, val) {
        htmlErrors = htmlErrors + '<li>' + val + '</li>';
    });
    if (htmlErrors) {
        $.modal(htmlErrors);
    }
}

// Stream Utilities
function getFirstStreamId() {
	var id = 0;
	if ($.exists($("#streamList li:first"))) {
		var elementId = $("#streamList li:first").attr("id");
		var id = parseInt(elementId.substring(elementId.lastIndexOf('_')+1));
	}	
	return id;
}
function getLastStreamId() {
	var id = 0;
	if ($.exists($("#streamList li.conversation_piece:last"))) {
		var elementId = $("#streamList li.conversation_piece:last").attr("id");
		var id = parseInt(elementId.substring(elementId.lastIndexOf('_')+1));
	}	
	return id;
}
function getId(element, pos) {
    var splitted = element.id.split("_");
    return splitted[pos||1];
}

//Current Page Reference
//copyright Stephen Chapman, 1st Jan 2005
//you may copy this function but please keep the copyright notice with it
function getURL() {
	var uri = new Object();
	var u = location.href + (location.href.indexOf('.htm')==-1?'.htm':'');
	
	uri.dir = u.substring(0, u.lastIndexOf('\/'));
	uri.protocol = uri.dir.substring(0, uri.dir.indexOf('\/\/')+2);
	uri.server = uri.dir; if (uri.server.substr(0,7) == 'http:\/\/') uri.server = uri.server.substr(7);
	uri.path = ''; var pos = uri.server.indexOf('\/'); if (pos > -1) {uri.path = uri.server.substr(pos+1); uri.server = uri.server.substr(0,pos);}
	uri.page = u.substring(uri.dir.length+1, u.length+1);
	pos = uri.page.indexOf('?');if (pos > -1) {uri.page = uri.page.substring(0, pos);}
	pos = uri.page.indexOf('#');if (pos > -1) {uri.page = uri.page.substring(0, pos);}
	uri.ext = ''; pos = uri.page.indexOf('.');if (pos > -1) {uri.ext =uri.page.substring(pos+1); uri.page = uri.page.substr(0,pos);}
	uri.file = uri.page;
	if (uri.ext != '') uri.file += '.' + uri.ext;
	if (uri.file == '') uri.page = 'index';
	uri.args = location.search.substr(1).split("?");
	uri.fullPath = '/' + uri.path + '/' + uri.page + '.' + uri.ext;
	return uri;
}
var uri = getURL();

function prettyPrintSize(bytes) {
    var size = bytes / 1024;
    if ( size < 1024) {
        size = Math.round(size*10)/10 + ' KB';
    } else if (size / 1024 < 1024) {
        size = Math.round((size*10) / 1024)/10 + ' MB';
    } else if (size / 1024 / 1024 < 1024) {
        size = Math.round((size*10) / 1024 / 1024)/10 + ' GB';
    }
    return size;
}

function jsonResponseDataToDictionary(list) {
    var data = new Dictionary();
    $.each(list, function(i, val) {
        data.put(val.key, val.value);
    });
    return data;
}

function Dictionary () {
    this.nodeObject = new Object();
    this.count=0;


    this.put = function (key, value) {
        obj = this.nodeObject;
        this.searchFlag = 0;

        var addFlag = true;
        for(var n in obj) {
            if(n == key) {
                obj[key] = value;
                addFlag = false;
            }
        }

        if(addFlag) {
            obj[key] = value;
            this.count++;
        }
    }

    this.get = function(key) {
        obj = this.nodeObject;

        return obj[key];
    }

    this.keys = function(){
        return this.nodeObject;
    }

    this.del = function(key){
        this.put(key, null);
        this.count--;
    }
   
    this.size = function(){
        return this.count;
    }
    
    this.getValues = function() {
        obj = this.nodeObject;
        var keys = [];
        var i = 0;
        for(var n in obj) {
            if (obj[n]!=null) {
                keys[i] = obj[n];
                i = i + 1;
            }
        }
        return keys;
    }
}

function url(url) {
    return context + url;
}
function profileImgUrl(id, hasPicture) {
    if(hasPicture) {
        return url('static/img/profiles/profile_' + id + '.jpg');
    } else {
        return url('static/img/profiles/profile_generic.jpg');
    }
}

function groupUrl(id) {
    return url('group/' + id);
}

function profileUrl(id) {
	return url('profile/' + id);
}

function profileIconTitle(act) {
	var name = (act.owner.id==currentUser.id ? youText : act.owner.name + (act.owner.lastName1? ' ' + act.owner.lastName1 + (act.owner.lastName2?' ' + act.owner.lastName2:'') :''));
	var currAct = (act.owner.currentActivityTitle?' (' + act.owner.currentActivityTitle + ')':'').replace(/"/g, '\\\'').replace(/\n/g, '');
	return '<div>' + name + ' ' + currAct + '</div>';
}

function showProfile(event) {
    id = getId(this);
    window.location = profileUrl(id);
    event.preventDefault();
}

function showGroup(event) {
    id = getId(this);
    window.location = groupUrl(id);
    event.preventDefault();
}

function l(){
    if (console) {
        console.log(arguments);
    }
}

// Dojo from/to ISOString
var _isoRegExp;

function fromISOString(_1,_2){
	if(!_isoRegExp){
	_isoRegExp=/^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
	}
	var _3=_isoRegExp.exec(_1);
	var _4=null;
	if(_3){
	_3.shift();
	if(_3[1]){
	_3[1]--;
	}
	if(_3[6]){
	_3[6]*=1000;
	}
	if(_2){
	_2=new Date(_2);
	dojo.map(["FullYear","Month","Date","Hours","Minutes","Seconds","Milliseconds"],function(_5){
	return _2["get"+_5]();
	}).forEach(function(_6,_7){
	if(_3[_7]===undefined){
	_3[_7]=_6;
	}
	});
	}
	_4=new Date(_3[0]||1970,_3[1]||0,_3[2]||1,_3[3]||0,_3[4]||0,_3[5]||0,_3[6]||0);
	var _8=0;
	var _9=_3[7]&&_3[7].charAt(0);
	if(_9!="Z"){
	_8=((_3[8]||0)*60)+(Number(_3[9])||0);
	if(_9!="-"){
	_8*=-1;
	}
	}
	if(_9){
	_8-=_4.getTimezoneOffset();
	}
	if(_8){
	_4.setTime(_4.getTime()+_8*60000);
	}
	}
	return _4;
};

function toISOString (_a,_b){
	var _=function(n){
	return (n<10)?"0"+n:n;
	};
	_b=_b||{};
	var _e=[];
	var _f=_b.zulu?"getUTC":"get";
	var _10="";
	if(_b.selector!="time"){
	var _11=_a[_f+"FullYear"]();
	_10=["0000".substr((_11+"").length)+_11,_(_a[_f+"Month"]()+1),_(_a[_f+"Date"]())].join("-");
	}
	_e.push(_10);
	if(_b.selector!="date"){
	var _12=[_(_a[_f+"Hours"]()),_(_a[_f+"Minutes"]()),_(_a[_f+"Seconds"]())].join(":");
	var _13=_a[_f+"Milliseconds"]();
	if(_b.milliseconds){
	_12+="."+(_13<100?"0":"")+_(_13);
	}
	if(_b.zulu){
	_12+="Z";
	}else{
	if(_b.selector!="time"){
	var _14=_a.getTimezoneOffset();
	var _15=Math.abs(_14);
	_12+=(_14>0?"-":"+")+_(Math.floor(_15/60))+":"+_(_15%60);
	}
	}
	_e.push(_12);
	}
	return _e.join("T");
};

function encodeToTooltip(source) {
	return (''+source).replace(/"/g, '&quot;').replace(/&#39;/g, '\\\'').replace(/\n/g, '<br/>');
}

$(document).ready(function(){

    // Loading indicator
    var loader = $('#loader');
    $().ajaxStart(function() {
        loader.show();
    }).ajaxStop(function() {
        loader.hide();
    }).ajaxError(function(event, XHR, ajaxOptions, thrownError) {
        if (XHR.status == 200 && XHR.responseText.indexOf('loginForm')!=-1) {
            // Session expired...
            window.location=window.location;
        } else {
            $.modal("Error requesting page " + ajaxOptions.url);
        }
    });
    
});