// Dates
Date.replaceChars.longMonths = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
Date.replaceChars.shortMonths = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

Date.formats = {
    secondago: "'Hace un segundo'",
    secondsago: "'Hace {0} segundos'",
    minuteago: "'Hace un minuto'",
    minutesago: "'Hace {0} minutos'",
    todayat1: "'Hoy a la' G':'i",
    todayat: "'Hoy a las' G':'i",
    yesterdayat1: "'Ayer a la' G':'i",
    yesterdayat: "'Ayer a las' G':'i",
    full1: "j 'de' F 'de' Y 'a la' G':'i",
    full: "j 'de' F 'de' Y 'a las' G':'i",
    dataEntry: "d/m/Y"
};

/*
 * Translated default messages for the jQuery validation plugin.
 * Language: ES
 * Author: David Esperalta - http://www.dec.gesbit.com/
 */
jQuery.extend(jQuery.validator.messages, {
  required: "Este campo es obligatorio.",
  remote: "Por favor, rellena esta campo.",
  email: "Por favor, escribe una dirección de correo válida",
  url: "Por favor, escribe una URL válida.",
  date: "Por favor, escribe una fecha válida.",
  dateISO: "Por favor, escribe una fecha (ISO) válida.",
  number: "Por favor, escribe un número entero válido.",
  digits: "Por favor, escribe sólo dígitos.",
  creditcard: "Por favor, escribe un número de tarjeta válido.",
  equalTo: "Por favor, escribe el mismo valor de nuevo.",
  accept: "Por favor, escribe una valor con una extensión aceptada.",
  maxlength: jQuery.format("Por favor, no escribas más de {0} caracteres."),
  minlength: jQuery.format("Por favor, no escribas menos de {0} caracteres."),
  rangelength: jQuery.format("Por favor, escribe un valor entre {0} y {1} caracteres."),
  range: jQuery.format("Por favor, escribe un valor entre {0} y {1}."),
  max: jQuery.format("Por favor, escribe un valor igual o menor que {0}."),
  min: jQuery.format("Por favor, escribe un valor igual o mayor que {0}.")
}); 

jQuery(function($){
    $.datepicker.regional['es'] = {
        closeText: 'Cerrar',
        prevText: '&#x3c;Ant',
        nextText: 'Sig&#x3e;',
        currentText: 'Hoy',
        monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
        'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
        monthNamesShort: ['Ene','Feb','Mar','Abr','May','Jun',
        'Jul','Ago','Sep','Oct','Nov','Dic'],
        dayNames: ['Domingo','Lunes','Martes','Mi&eacute;rcoles','Jueves','Viernes','S&aacute;bado'],
        dayNamesShort: ['Dom','Lun','Mar','Mi&eacute;','Juv','Vie','S&aacute;b'],
        dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','S&aacute;'],
        dateFormat: 'dd/mm/yy', firstDay: 1,
        isRTL: false};
    $.datepicker.setDefaults($.datepicker.regional['es']);
});

// Message templates 
var activityMessageTemplates = {
        STATUS: "{renderedMessage}", 
        COMMENT: "<a class='TypeLinkThread' id='commentedTo_{parentId}' href='#' onMouseOver=\"Tip('{replyToRenderedMessage}', BALLOON, true, ABOVE, true, OFFSETX, -30)\" onmouseout='UnTip()'>en respuesta a</a> <a class='profile TypeLinkProfile' id='commenterProfile_{replyToOwnerId}' href='#'>{replyToOwnerName}:</a> {renderedMessage}",
        EVENT: "{renderedMessage} (desde el {startDateFormatted} hasta el {endDateFormatted})", 
        NEWGROUP: "ha creado el grupo <a class='TypeLinkGroup' id='group_{data_groupId}' href='#'>{data_groupName}</a>.",
        NEWUSER: "se a unido a la red."
}