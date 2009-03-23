var flashvars = {};
flashvars.id = 'air_badge';
flashvars.airversion = '1.5';
flashvars.appname = 'pingFit';
flashvars.appurl = '---AIRURL---';
flashvars.appid = 'com.pingFit';
flashvars.pubid = '';
flashvars.appversion = '---VERSION---';
flashvars.installarg = '---FLASHVARS---';
flashvars.launcharg = '---FLASHVARS---';
flashvars.helpurl = 'help.html';
flashvars.hidehelp = 'true';
flashvars.skiptransition = 'false';
flashvars.titlecolor = '#aaff';
flashvars.buttonlabelcolor = '#aaff';
flashvars.appnamecolor = '#aaff';


var params = {};
params.wmode = 'transparent';
params.menu = 'false';
params.quality = 'high';

var attributes = {};

swfobject.embedSWF('badgefiles/AIRInstallBadge.swf', 'badge_div', '215', '180', '9.0.115', 'badgefiles/expressInstall.swf', flashvars, params, attributes);