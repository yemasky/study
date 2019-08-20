<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<title>${Title}</title>
<script>var __VERSION = '${__VERSION}'; var baseDateTime = '${thisDateTime}', baseSeconds = 0;function setBaseSeconds() {baseSeconds++;} setInterval(setBaseSeconds, 1000);</script>
<link rel="stylesheet" href="${__RESOURCE}styles/app.min.css?${__VERSION}" type="text/css" />
<!-- commom -->
<script src="${__RESOURCE}jquery-3.4.1.min.js?${__VERSION}"></script>
<script>
    var __RESOURCE = '${__RESOURCE}';
//jQuery 插件
(function ($) {
    $.serializeFormat = function (id) {
		var serialize = $(id).serializeArray();var serializeFormat = {},n_name='',n_n_name='';//console.log(serialize);
        for(var i in serialize) {
			var name = serialize[i].name;
			var bIndex = name.indexOf('[');
			if(bIndex > 0) {
				var arrayName = name.split('[');
				for(var j = 0; j < arrayName.length; j++) {
					var n_key = arrayName[j];
					var n_keyIndex = n_key.indexOf(']');
					if(n_keyIndex > 0) {n_key = n_key.substring(0, n_keyIndex);}
					if(j == 0) {if(typeof(serializeFormat[n_key]) == 'undefined') serializeFormat[n_key] = {};n_name = n_key;}
					if(j == 1) {
						if(typeof(serializeFormat[n_name][n_key]) == 'undefined') serializeFormat[n_name][n_key] = {};n_n_name = n_key;
						if(arrayName.length == 2) serializeFormat[n_name][n_key] = serialize[i].value;
					}
					if(j == 2) {serializeFormat[n_name][n_n_name][n_key] = serialize[i].value;}//最多2级
				}//if(item_json != '') serializeFormat = eval('('+item_json+')');//var eIndex = name.indexOf(']');//var n_name = name.substring(0, bIndex);var key = name.substring(bIndex+1, eIndex);//if(typeof(serializeFormat[n_name]) == 'undefined') serializeFormat[n_name] = {};//serializeFormat[n_name][key] = serialize[i].value;
			} else {
				serializeFormat[name] = serialize[i].value;
			}
		}
		function explode(name) {
			var serializeFormat = {};var bIndex = name.indexOf('[');var eIndex = name.indexOf(']');
			var n_name = name.substring(0, bIndex);var key = name.substring(bIndex+1, eIndex);
			if(typeof(serializeFormat[n_name]) == 'undefined') serializeFormat[n_name] = {};
			serializeFormat[n_name][key] = serialize[i].value;
		}
        return serializeFormat;
    }
})(jQuery);
</script>
<script src="${__RESOURCE}angular.min.1.6.8.js?${__VERSION}"></script>
<script src="${__RESOURCE}angular-lib.min.js?${__VERSION}"></script>
<script src="${__RESOURCE}angular-lib-extend.min.js?${__VERSION}"></script>
<script src="${__RESOURCE}vendor/modules/angular-strap/angular-strap.min.js?${__VERSION}"></script>
<script src="${__RESOURCE}vendor/modules/angular-smart-table/smart-table.min.2.1.10.js?${__VERSION}"></script>   
<script>//'ui.jp',,'angular-popups', 'ui.select'
angular.module("app",['ngMessages','ngAnimate','ngCookies','ngResource','ngRoute','ngSanitize','ngTouch','pascalprecht.translate','ngStorage','ui.router','ui.utils','mgcrea.ngStrap','oc.lazyLoad','ui.load','angular-loading-bar','smart-table'
]);
var app = angular.module("app").config(["$controllerProvider","$compileProvider","$filterProvider","$provide", "$ocLazyLoadProvider",
	function($controllerProvider, $compileProvider, $filterProvider, $provide) {
		app.controller = $controllerProvider.register, app.directive  = $compileProvider.directive, 
		app.filter     = $filterProvider.register,     app.factory    = $provide.factory, 
		app.service    = $provide.service,             app.constant   = $provide.constant,
		app.value      = $provide.value
}]).config(["$translateProvider", function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: "${__RESOURCE}i18n/langs/",
        suffix: ".json"
    }), $translateProvider.preferredLanguage("en"), $translateProvider.useLocalStorage(),$translateProvider.useSanitizeValueStrategy('sanitize');
}]).factory("$httpFactory", function($http, $modal, $translate, $alert, cfpLoadingBar) {
    var factory = {};
    factory.post = function($url, $scope, callBack) {//,'Content-Type': 'application/json'
        //$http.defaults.headers.common.ajaxrequest = true;
        $http.post($url, $scope.param, {timeout : 5000}).then(function(response) {
			if(response.data.success == 0) {
                var message = $translate.instant("error.code."+response.data.code) + ' ' + response.data.message;
				var message_ext = $('label[for="'+response.data.item[0]+'"]').text();
				if(message_ext != null && message_ext != '') message += '［'+message_ext + '］数据错误！';
				$modal({title: 'Error', content: message, templateUrl: '/modal-warning.html', show: true});
			} else if(response.data.success == -1) {
                var message = $translate.instant("notice.code."+response.data.code) + ' ' + response.data.message;
				var message_ext = $('label[for="'+response.data.item[0]+'"]').text();
				if(message_ext != null && message_ext != '') message += '［'+message_ext + '］数据错误！';
				$modal({title: 'Notice', content: message, templateUrl: '/modal-notice.html', show: true});
			} else if(response.data.success == 1) {
				//
			} else {
				$modal({title: 'Error', content: '请求错误，请检查!', templateUrl: '/modal-warning.html', show: true});
			}
			callBack(response);
            cfpLoadingBar.complete();
		}, function(response){
			if(response != null && typeof(response['status']) != 'undefined' && response['status'] == -1) {
				$modal({title: 'Time out', content: '连接服务器超时，时间5秒!', templateUrl: '/modal-warning.html', show: true});
			} else {
				$modal({title: 'Error', content: '请求错误数据解析返回出错，请检查!', templateUrl: '/modal-warning.html', show: true});
			}
			callBack({'data':{'success':0,'code':'000000'}});
            cfpLoadingBar.complete();
		});
    }
	factory.get = function($url, $scope, callBack) {//,'Content-Type': 'application/json'
        //$http.defaults.headers.common.ajaxrequest = true;
        $http.get($url, $scope.param, {timeout : 5000}).then(function(response) {
			if(response.data.success == 0) {
				var message = $scope.getErrorByCode(response.data.code);
				$modal({title: 'Error', content: message, templateUrl: '/modal-warning.html', show: true});
			} else if(response.data.success == 1) {
				//
			} else {
				$modal({title: 'Error', content: '请求错误，请检查!', templateUrl: '/modal-warning.html', show: true});
			}
			callBack(response);
            cfpLoadingBar.complete();
		}, function(response){
			if(response != null && typeof(response['status']) != 'undefined' && response['status'] == -1) {
				$modal({title: 'Time out', content: '连接服务器超时，时间5秒!', templateUrl: '/modal-warning.html', show: true});
			} else {
				$modal({title: 'Error', content: '请求错误数据解析返回出错，请检查!', templateUrl: '/modal-warning.html', show: true});
			}
			callBack({'data':{'success':0,'code':'000000'}});
            cfpLoadingBar.complete();
		});
    }
	factory.header = function($key, $value) {
		var header = {};header[$key] = $value;
		$http.defaults.headers.common = header;//$http.defaults.headers.common.Authorization = JSON.stringify(header);
	}
	factory.deleteHeader = function($key) {
		if(typeof($http.defaults.headers.common[$key]) != 'undefined') delete $http.defaults.headers.common[$key];
	}
    return factory;
}).service("$httpService", function($httpFactory){
    this.post = function($url, $scope, callBack) {return $httpFactory.post($url, $scope, callBack);}
	this.get = function($url, $scope, callBack) {return $httpFactory.get($url, $scope, callBack);}
	this.header = function($key, $value) {return $httpFactory.header($key, $value);}
	this.deleteHeader = function($key) {return $httpFactory.deleteHeader($key);}
}).directive("triggerLoading", function(){
	return {
		restrict:'A',
		link:function($scope, $element, $attr){
			$scope.prevText = $element.html();
			$scope.$watch(function(){
				return $scope.$eval($attr.triggerLoading);
			},function($value){
				if(angular.isDefined($value)){
                    //element.toggleClass('disabled',value);
					$value ? $element.attr('disabled', true) : $element.removeAttr('disabled');
					$element.html(($value ? '<div class="spinner-icon"></div>'+$attr.btnLoadingText : $scope.prevText));
				}
			});
		}
	}
}).directive('stringToNumber', function() {
  	return {
    require: 'ngModel',
    link: function($scope, $element, $attrs, ngModel) {
		ngModel.$parsers.push(function(value) {
        	return '' + value;
		});
		ngModel.$formatters.push(function(value) {
			return parseFloat(value);
		});
    }
  };
}).directive('toggleClass', function(){
    return {
        restrict: 'A',
        scope: {
            toggleClass: '@'
        },
        link: function($scope, $element, $attr){
            var toggleTarget = $attr.classTarget;
            $element.on('mouseover', function(){
                if(typeof toggleTarget == 'undefined' || toggleTarget == '') {
                    $element.addClass($scope.toggleClass);
                } else {
                    $element.find(toggleTarget).addClass($scope.toggleClass);
                }
            });
            $element.on('mouseout', function(){
                if(typeof toggleTarget == 'undefined' || toggleTarget == '') {
                    $element.removeClass($scope.toggleClass);
                } else {
                    $element.find(toggleTarget).removeClass($scope.toggleClass);
                }
            });
        }
    };
});
app.filter('propsFilter', function() {
    return function(items, props) {
        var out = [];
        if (angular.isArray(items)) {
          items.forEach(function(item) {
            var itemMatches = false;
            var keys = Object.keys(props);
            for (var i = 0; i < keys.length; i++) {
				var prop = keys[i];
				var text = props[prop].toLowerCase();
				if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
					itemMatches = true;
					break;
				}
            }
            if (itemMatches) {
              out.push(item);
            }
          });
        } else {
          // Let the output be the input untouched
			out = items;
        }
        return out;
    };
});
angular.module("app").constant("MODULE_CONFIG", [
    {name: "ui.select",module: !0,files: ["${__RESOURCE}vendor/modules/angular-ui-select/select.min.js", "${__RESOURCE}vendor/modules/angular-ui-select/select.min.css"]}, 
    {name: "easyPieChart",module: !1,files: ["${__RESOURCE}vendor/jquery/easypiechart/jquery.easy-pie-chart.js"]}
]).config(["$ocLazyLoadProvider", "MODULE_CONFIG", function($ocLazyLoadProvider, MODULE_CONFIG) {
    $ocLazyLoadProvider.config({
        debug: !1,
        events: !1,
        modules: MODULE_CONFIG
    })
}]);
angular.module("app").directive("uiNav", ["$timeout", function() {
    return {
        restrict: "AC",
        link: function(a, b) {
            b.find("a").bind("click", function() {
                var b = angular.element(this).parent();
                b.parent().find("li").removeClass("active"), b.toggleClass("active"), b.find("ul") && (a.app.asideCollapse = !1)
            })
        }
    }
}]),
angular.module("app").directive("uiFullscreen", ["$ocLazyLoad", "$document", function(a, b) {
    return {
        restrict: "AC",
        link: function(c, d, e) {
            d.addClass("hide"), a.load("/resource/vendor/libs/screenfull.min.js").then(function() {
                screenfull.enabled && d.removeClass("hide"), d.bind("click", function() {
                    var a;
                    e.target && (a = angular.element(e.target)[0]), screenfull.toggle(a)
                });
                var a = angular.element(b[0].body);
                b.on(screenfull.raw.fullscreenchange, function() {
                    screenfull.isFullscreen ? a.addClass("fullscreen") : a.removeClass("fullscreen")
                })
            })
        }
    }
}]);
app.run(["$rootScope", "$state", "$stateParams", "$location", "$httpService", function($rootScope, $state, $stateParams, $location, $httpService) {
    $rootScope.$state = $state, $rootScope.$stateParams = $stateParams;
	//<c:if test="${noLogin==1}">
	//
	$location.path('/login');
	//</c:if>
	//<c:if test="${noLogin==0}">
	var current_path = $location.path();
	current_path = current_path == '' || current_path == '/login' ? '/app/home' : current_path;
	$httpService.header('refresh', '1');
	$location.path(current_path);//'/app/home'
	//$httpProvider.defaults.headers.common = {'refresh' : '0'};
	//</c:if>
	//
}]).config(function($controllerProvider,$compileProvider,$filterProvider,$provide){        
    app.register = {
        //得到$controllerProvider的引用
        controller : $controllerProvider.register,
        //同样的，这里也可以保存directive／filter／service的引用
        directive: $compileProvider.directive,
        filter: $filterProvider.register,
        service: $provide.service
    };
}).config(["$stateProvider", "$urlRouterProvider", "$httpProvider", function($stateProvider, $urlRouterProvider, $httpProvider) {
	//$urlRouterProvider.otherwise("/app/dashboard"),
	$httpProvider.defaults.headers.common = {'ajaxRequest' : true};
	function randomUrl(channel) {
		if(angular.isDefined($httpProvider.defaults.headers.common.ajaxRequest))
			delete $httpProvider.defaults.headers.common['ajaxRequest'];
		return '${__WEB}app.do?channel=' + channel+'&_='+Math.random();
	};
	$urlRouterProvider.otherwise("/app/home"),
	$stateProvider.state("app", {
		"abstract": !0,
        url: "/app",
        views: {
            "": {templateUrl: "/app/layout.html"},
            header_menu: {
                templateUrl: function() {
					return "/app/header/aside.nav.menu.html";//;
				}
            }
		}
    }).state("app.home", {
        url: "/home",
		templateUrl: function() {
			return randomUrl('${home_channel}');
			//return "${__WEB}app.do?channel=${home_channel}";
		},
        controller: function ($scope) {
			//console.log($scope);
        }
    }).state("app.logout", {
        url: "/logout",
        templateUrl: function ($routeParams) {
            return "/login.html";
        },
        controller: function ($httpService, $location) {
            $httpService.post('${__WEB}app.do?action=logout', '', function(response){
                $location.path('/login');
            });
        }
    }).state('app.Booking', {
        url: "/booking/:view/:channel", //url: "/role/edit?id",
        templateUrl: function($routeParams, $rootScope, $scope) {
			return 'resource/views/Booking/'+$routeParams.view+'.html?${__VERSION}';
        },
		controller: function($rootScope, $scope, $ocLazyLoad, $httpService) {
		}
		/*,resolve: {
            deps: ["$ocLazyLoad", function($ocLazyLoad) {
                //return $ocLazyLoad.load(['resource/scripts/controllers/Booking/Room.js']);
            }]
        }
        data: {pageTitle: '编辑角色'},*/
    }).state('app.Management', {
        url: "/Management/:view/:channel", //url: "/role/edit?id",
        templateUrl: function($routeParams) {
            return 'resource/views/Management/'+$routeParams.view+'.html?${__VERSION}';
        },
		controller: function() {
			
		}
    }).state('app.Setting', {
        url: "/Setting/:view/:channel", //url: "/role/edit?id",
        templateUrl: function($routeParams, $rootScope, $scope) {
			var view = $routeParams.view;//有view访问静态文件
			if(view != '') return 'resource/views/Setting/'+$routeParams.view+'.html?${__VERSION}';
            return randomUrl($routeParams.channel); 
        },
		controller: function($rootScope, $scope, $ocLazyLoad, $httpService) {
		}
    }).state('app.Channel', {
        url: "/Channel/:channel", //url: "/role/edit?id",
        templateUrl: function($routeParams) {
            return randomUrl($routeParams.channel);
        },
		controller: function() {
		}
    }).state('app.Test', {
        url: "/Test/:test", //url: "/role/edit?id",
        templateUrl: function($routeParams) {
            return 'resource/views/Test/'+$routeParams.test+'.html?${__VERSION}';
        },
		controller: function() {
		}
    }),
	$stateProvider.state("login", {
        url: "/login",templateUrl: "/login.html",
        resolve: {
            deps: ["$ocLazyLoad", function($ocLazyLoad) {
                return $ocLazyLoad.load(["${__RESOURCE}vendor/libs/md5.min.js"]);
            }]
        }
    })
}]);
app.controller('MainController',["$rootScope","$scope","$translate","$localStorage","$window","$location","$httpService","$modal","$tooltip","$filter","$alert","$interval","$log", 
	function($rootScope,$scope,$translate,$localStorage,$window,$location,$httpService,$modal,$tooltip,$filter,$alert,$interval,$log) {
		function matchNavigator($window) {
			var navigatorInfo = $window.navigator.userAgent || $window.navigator.vendor || $window.opera;
			return /iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/.test(navigatorInfo)
		}
		var isIE = !!navigator.userAgent.match(/MSIE/i) || !!navigator.userAgent.match(/Trident.*rv:11\./);
		isIE && angular.element($window.document.body).addClass("ie"); 
		matchNavigator($window) && angular.element($window.document.body).addClass("smart");
		$scope.app = {
			name: "iHotelWork", version: "${__VERSION}",
			color: {
				primary: "#155abb",info: "#2772ee",success: "#4bb622",warning: "#f88311",danger: "#e11144",
				inverse: "#a66bee",light: "#f1f2f3",dark: "#202a3a"
			},
			settings: {
				headerColor: "bg-primary",headerFixed: !0,headerShadow: !0,asideColor: "bg-dark lt",asideTop: !0
			}
		};
		$scope.options = {
			headerColor: ["bg-primary lt", "bg-primary ", "bg-primary dk", "bg-info lt", "bg-info", "bg-info dk", "bg-success lt", "bg-success ", "bg-success dk", "bg-inverse lt", "bg-inverse ", "bg-inverse dk", "bg-dark lt", "bg-dark", "bg-dark dk ", "bg-black ", "bg-black dk", "bg-white box-shadow-md"],
			asideColor: ["bg-primary dk", "bg-info dk", "bg-success dk", "bg-dark lt", "bg-dark", "bg-dark dk", "bg-black lt", "bg-black", "bg-black dk", "bg-white", "bg-light", "bg-light dk"]
		}; 
		$scope.setHeaderColor = function(color) {
			$scope.app.settings.headerColor = color
		}; 
		$scope.setAsideColor = function(color) {
			$scope.app.settings.asideColor = color
		}; 
		angular.isDefined($localStorage.appSettings) ? $scope.app.settings = $localStorage.appSettings : $localStorage.appSettings = $scope.app.settings; 
		$scope.$watch("app.settings", function() {
			$localStorage.appSettings = $scope.app.settings
		}, !0);
		$scope.langs = {en: "English",zh_CN: "中文"}; 
		$scope.select_lang = $scope.langs[$translate.proposedLanguage()] || "English";
		$scope.setLang = function(lang) {
			$scope.select_lang = $scope.langs[lang], $translate.use(lang)
		};
		//basevaule
		$scope._baseDateTime = function() {
			var unix_time =  new Date(baseDateTime.replace(/-/g,'/')).getTime() - 0;
			return unix_time - 0 + baseSeconds;
            //return baseDateTime.substr(0, 10);
		};
        $scope.getDay = function(format) {
            if(angular.isUndefined(format)) format = "yyyy-MM-dd";
            return $filter("date")($scope._baseDateTime(), format, 'UTC +8');
        }
        $scope.__RESOURCE = __RESOURCE;
		$scope._resource = '${__RESOURCE}';
		//
        $scope.switchChannel = function(channel_id) {
            console.log($scope.employeeChannel[channel_id]);
        }
		$rootScope.employeeMenu = {};$scope.hashEmployeeModule = {};
		$scope.setMenu = function($menus, $module_channel) {
			if($menus == '') $menus = $rootScope.employeeMenu;
			var channels = {}, channel_i = 0, menus = {}, hashEmployeeModule = {};
			if($menus != null && $menus != '') {
				for(var i in $menus) {
                    hashEmployeeModule[$menus[i].module_id] = $menus[i];
					if($menus[i].module_father_id == '-1') {
						channels[channel_i] = $menus[i];channel_i++;
					} else {
						var father_id         = $menus[i].module_father_id;
						var module_id         = $menus[i].module_id;
						var submenu_father_id = $menus[i].submenu_father_id;
						//if(typeof(menus[father_id]) == 'undefined') menus[father_id] = {};
						//if(typeof(menus[father_id][module_id]) == 'undefined') menus[father_id][module_id] = {};
						if(father_id == '0') {
							var children = {}, have_children = 0;
							if(typeof(menus[module_id]) != 'undefined') {//father_id 比 module_id 要大，在后面添加的 已经添加了children
								children = menus[module_id]['children'];have_children = 1;
							}
							menus[module_id] = $menus[i];//father_id == 0 第一行菜单
							menus[module_id]['hide'] = 'hide';
							if(menus[module_id]['module_channel'] == $module_channel) menus[module_id]['hide'] = '';
							menus[module_id]['children'] = children;
							menus[module_id]['have_children'] = have_children;
						} else {//
							if(typeof(menus[father_id]) == 'undefined') {menus[father_id] = {};menus[father_id]['children']={};}
							if(submenu_father_id == '0') {
								menus[father_id]['children'][module_id] = $menus[i];
								menus[father_id]['children'][module_id]['submenu'] = {};
								menus[father_id]['children'][module_id]['have_children'] = 0;
								menus[father_id]['have_children'] = 1;
							} else {
								menus[father_id]['children'][submenu_father_id]['submenu'][module_id] = $menus[i];
								menus[father_id]['children'][submenu_father_id]['have_children'] = 1;
							}
						}
					}
				}
			}
			$scope.menus = menus;$scope.channels = channels;
            $scope.hashEmployeeModule = hashEmployeeModule;
		};
		$scope.checkMenuData = function($common) {
			if($common == null || $common == '') return;
			if(typeof($common.employeeMenu) != 'undefined' && $common.employeeMenu != null && $common.employeeMenu != '') 
				$scope.setMenu($common.employeeMenu, $common.module_channel);
		};
		$scope.reconvertChinese = function (str) {
			str = str.replace(/(&#)(\d{1,6});/gi,function($0){
				return String.fromCharCode(parseInt(escape($0).replace(/(%26%23)(\d{1,6})(%3B)/g,"$2")));
			});
			return str;
		};
		$scope.getErrorByCode = function($errorCode) {
			return $translate.instant("error.code."+$errorCode);
		};
		$scope.setChannelMenu = function($module_channel) {
			$scope.setMenu($rootScope.employeeMenu, $module_channel);
		};
		$rootScope._self_module = '';
		$scope.setActionNavName = function(module_id) {
			var menus = $scope.menus, nav = '', _self_module = $rootScope._self_module;
			if(typeof(menus[module_id]) != 'undefined') {
				nav = menus[module_id].module_name;
			} else if(_self_module != '' && _self_module.module_id == module_id) {
				nav = _self_module.module_name;
				module_id = _self_module.module_father_id;
			} else {
			}
			if(menus != '') {
				for(var i = 0; i<=5; i++) {
					if(typeof(menus[module_id]) == 'undefined') continue;
					var father_id = menus[module_id].module_father_id;
					if(typeof(menus[father_id]) != 'undefined') {
						var href = 'href="/#!/app/'+menus[father_id].module_channel+'/'+menus[father_id].url+'" '
						          +'ng-click="setActionNavName('+father_id+')"';
						nav = '<a '+href+'>' + menus[father_id].module_name + '</a> <i class="fa fa-angle-double-right"></i> ' + nav;
						module_id = father_id;
					} else {
						break;
					}
				}
			}
            if(nav == '') nav = 'Welcome';//angular.element('#action_nav_name').html(nav);
			$scope.action_nav_name = '' + nav;
		};
        $scope.getChannelModule = function(module_id) {
            return $scope.hashEmployeeModule[module_id];
        }
        $rootScope.defaultChannel = {};
        $rootScope.business_day = $scope.business_day = '';
		$scope.setCommonSetting = function(common) {//$common == null?
			if(angular.isDefined(common) && common != '') {
				if(typeof(common.employeeMenu) != 'undefined') {
					$rootScope.employeeMenu = common.employeeMenu;
					if(common.employeeMenu != null && common.employeeMenu != '') $scope.setMenu(common.employeeMenu, common.module_channel);
				}
				if(typeof(common.employeeInfo) != 'undefined') {
					$rootScope.employeeInfo = common.employeeInfo;
				}
                if(typeof(common.channelSettingList) != 'undefined') {
					$rootScope.channelSettingList = common.channelSettingList;
				}
				if(typeof(common.employeeChannel) != 'undefined') {
					for(var i in common.employeeChannel) {
                        if(common.employeeChannel[i].default == 1) {
                            $rootScope.defaultChannel = common.employeeChannel[i];
                            $httpService.header('default', common.employeeChannel[i].id);break;
                        }
                    }
					$rootScope.employeeChannel = common.employeeChannel;
				}
				if(typeof(common._self_module) != 'undefined') {
					$rootScope._self_module = common._self_module;
				}
                if(angular.isDefined(common.business_day)) $rootScope.business_day = $scope.business_day = common.business_day;
				if(angular.isDefined(common.__module_id)) $scope.setActionNavName(common.__module_id);
			}
		};
        $scope.getBusinessDay = function(format) {
            if(angular.isUndefined(format)) format = "yyyy-MM-dd";
            var businessDate = new Date($rootScope.business_day).getTime();
            return $filter("date")(businessDate, format);
        };
		$rootScope.employeeChannel = {};
        $scope.setThisChannel = function(channel) {
			var thisChannel = [];
			var employeeChannel = $rootScope.employeeChannel;
			var k = 0, thisChannel_id = '';
			if(channel == 'Hotel') {
				/*thisChannel[0] = {};
				thisChannel[0]['id'] = 0;
				thisChannel[0]['channel_id'] = 0;
				thisChannel[0]['channel_name'] = '<i class="fa fa-lightbulb-o"></i> 所有酒店适用</a>';
				k++;*/
			};
			for(var i in employeeChannel) {
				if(employeeChannel[i].channel == channel) {
					thisChannel[k] = {};
					thisChannel[k]['id'] = employeeChannel[i].id;
					thisChannel[k]['channel_id'] = employeeChannel[i].channel_id;
					thisChannel[k]['channel_name'] = employeeChannel[i].channel_name;
                    thisChannel[k]['channel_father_id'] = employeeChannel[i].channel_father_id;
					if(employeeChannel[i].default == 1) thisChannel_id = employeeChannel[i].id
					k++;
				}
			}
			if(thisChannel_id == '' && typeof(thisChannel[0]) != 'undefined') thisChannel_id = thisChannel[0]['value'];
			$scope.thisChannel_id = thisChannel_id;
			$scope.thisChannel = thisChannel;
		};
		$scope.getChannelSetting = function(channel_id, key) {
			if(angular.isDefined($rootScope.channelSettingList[channel_id][key])) {
				return $rootScope.channelSettingList[channel_id][key];
			}
			if(angular.isDefined($rootScope.channelSettingList[channel_id])) {
			   return $rootScope.channelSettingList[channel_id];
			}
			return null;
		}
		////*********************************////
		$scope.redirect = function(url) {
			$location.path(url);
		};
		$scope._function = function(){};$scope._param = {};
		$scope.setController = function(_function, _param) {
			$scope._function = _function; $scope._param = _param;
		};
		$scope.getController = function() {
			return {'_function':$scope._function, '_param':$scope._param};
		};
		$scope.setHWPercent = function(className, h) {
			var height = window.screen.height;
			var newHeight = height * h;
		}
		//日历
		$scope.setCalendar = function (year, month) {
			var days = {}, k = 0;days[k] = {};
			//得到表示指定年和月的1日的那个时间对象
			var date = new Date(year, month - 1, 1);
			//1.先添加响应的空白的li:这个月1号是星期几，就添加几个空白的li
			var dayOfWeek = date.getDay(); //得到1日是星期几
			for(var i = 0; i < dayOfWeek; i++) {days[k][i] = "";}
			var daysOfMonth = new Date(year, month, 0).getDate();//计算一个月有多少天
			//2. 从1号开始添加li
			var j = dayOfWeek;
			for(var i = 1; i <= daysOfMonth; i++) {
				//days.push(i);
				date = new Date(year, month - 1, i);dayOfWeek = date.getDay();
				if(dayOfWeek == 0 && i > 1) {k++;j = 0;}
				if(typeof(days[k]) == 'undefined') days[k] = {};
				var day = i;
				if(i < 10) day = '0'+i;
				days[k][j] = day;j++;
			}
			if(j<6) {for(j; j<=6; j++) {days[k][j] = "";}}//补全
			return days;
		}
		$scope.arithmetic = function(value, symbol, parameter, to_fixed) {
			var value = value - 0;var parameter = parameter - 0;
			var result = eval('('+value+symbol+parameter+')');
			result = parseFloat(result.toPrecision(12));
			if(typeof(to_fixed) != 'undefined' && to_fixed >= 0) result = result.toFixed(to_fixed);
			return result;
		}
		$scope.weekday = new Array(7);
		$scope.weekday[0]="日";$scope.weekday[1]="一";$scope.weekday[2]="二";
		$scope.weekday[3]="三";$scope.weekday[4]="四";$scope.weekday[5]="五";$scope.weekday[6]="六";
        $scope.loading = $alert({scope : $scope, placement: 'top', type: 'info', templateUrl: '/loading.html', show: false});
        $scope.successAlert = $alert({scope : $scope, title: 'Success', templateUrl: '/resource/views/Common/successAlertRound.html', content: '操作成功！', placement: 'top-left', duration: 3, type: 'success', show: false});
        var vm = $scope.vm = {};
        vm.value = 0;
        $scope.startProgressBar = function(index) {
            $scope.vm.value = index;
            var start = $interval(function(){
                $scope.vm.value = ++index;
                if (index > 99) {$interval.cancel(start);}
                if (index == 60) {index = 99;}
            }, 30);
        };
		vm.isLoad = 90;
		$scope.startLoading = function(index) {
            $scope.vm.isLoad = index;
            var start = $interval(function(){
                $scope.vm.isLoad = ++index;
                if (index >= 100) {$scope.loading.hide();}
                if (index >= 95) {$interval.cancel(start);}
                if (index == 60) {index = 95;}
            }, 3);
        };
        $scope.successAlert.startProgressBar = function() {
            $scope.successAlert.show();$scope.startProgressBar(0);
        };
		$scope.loading.start = function() {
            $scope.loading.show();$scope.startLoading(10);
        };
        $scope.loading.percent = function() {$scope.startLoading(99);};
		$scope.confirm = function(param) {//content, confirmCallback, param
			var content = angular.isDefined(param.content)?param.content : '';var confirmCallback = angular.isDefined(param.callback)?param.callback : null;
			var callbackParam = angular.isDefined(param.param)?param.param : null;
			$alert({scope : $scope, title: 'Notice', templateUrl: '/resource/views/Common/modalConfirm.html', content: content, placement: 'top', type: 'success', show: true, controller : function($scope) {$scope.callback = function() {if(confirmCallback){
				if(callbackParam != null) {confirmCallback(callbackParam);}else{confirmCallback();}};}}});
		}
		
}]);
//login 
app.controller("LoginController",function($rootScope, $scope, $httpService, $modal, $location, $translate, $tooltip, $log){
	var loginTooltip = '';
	$scope.login = function(valid) {
        if(valid == false) return;
        $scope.login.error_message = '';
		$scope.beginLoading =! $scope.beginLoading;
		var password = $scope.param.password;
		$scope.param.password = md5(md5(password));
		$httpService.deleteHeader('ajaxRequest');
		$httpService.post('${__WEB}app.do?method=checkLogin', $scope, function(result){
			$scope.beginLoading =! $scope.beginLoading;
			if(result.data.success == 1) {
                $location.path('/app/home');
				//$rootScope.employeeMenu = result.data.item.loginEmployee.employeeMenu;//$scope.module_channel = result.data.item.module_channel;
				//$rootScope.employeeInfo = result.data.item.loginEmployee.employeeInfo;
				var employeeChannel = result.data.item.loginEmployee.employeeChannel;
				for(var i in employeeChannel) {if(employeeChannel[i].default == 1) {$httpService.header('default', employeeChannel[i].id);break;}}
				//$rootScope.employeeChannel = employeeChannel;
                //$scope.setMenu($rootScope.employeeMenu, result.data.item.module_channel);
				$scope.setCommonSetting(result.data.item.loginEmployee);
            } else {
			    var message = $scope.reconvertChinese($translate.instant("error.code."+result.data.code));
                $scope.param.password = password;
                if(loginTooltip == ''){ 
					loginTooltip = $tooltip(angular.element('#email'), 
						{title: '<i class="fa fa-warning b-warning text-warning"></i> '+ message, show:true, placement: 'top-left', type: 'waring', html:true});
                    $('#email').on('mouseover', function (e) {
                        
                    });
                } else {loginTooltip.show();}
            }
		});
	}
});	
//plugin
app.directive('pageSelect', function() {
      return {
        restrict: 'E',
        template: '<input type="text" class="select-page" ng-model="inputPage" ng-change="selectPage(inputPage)">',
        link: function(scope, element, attrs) {
          scope.$watch('currentPage', function(c) {
            scope.inputPage = c;
          });
        }
      }
});
</script>
<script src="${__RESOURCE}vendor/libs/moment.min.js?${__VERSION}"></script>
<script src="${__RESOURCE}vendor/libs/daterangepicker.js?${__VERSION}"></script>
<link rel="stylesheet" href="${__RESOURCE}vendor/libs/daterangepicker.css?${__VERSION}" type="text/css" />
