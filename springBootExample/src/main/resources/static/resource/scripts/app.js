var app = angular.module("app").config(["$controllerProvider", "$compileProvider", "$filterProvider", "$provide", function(a, b, c, d) {
    app.controller = a.register, app.directive = b.directive, app.filter = c.register, app.factory = d.factory, app.service = d.service, app.constant = d.constant, app.value = d.value
}]).config(["$translateProvider", function(a) {
    a.useStaticFilesLoader({
        prefix: "resource/Heymtro/i18n/",
        suffix: ".js"
    }), a.preferredLanguage("en"), a.useLocalStorage()
}]);
angular.module("app").constant("MODULE_CONFIG", [{
    name: "ui.select",
    module: !0,
    files: ["vendor/modules/angular-ui-select/select.min.js", "vendor/modules/angular-ui-select/select.min.css"]
}, {
    name: "textAngular",
    module: !0,
    files: ["vendor/modules/textAngular/textAngular-sanitize.min.js", "vendor/modules/textAngular/textAngular.min.js"]
}, {
    name: "vr.directives.slider",
    module: !0,
    files: ["vendor/modules/angular-slider/angular-slider.min.js", "vendor/modules/angular-slider/angular-slider.css"]
}, {
    name: "angularBootstrapNavTree",
    module: !0,
    files: ["vendor/modules/angular-bootstrap-nav-tree/abn_tree_directive.js", "vendor/modules/angular-bootstrap-nav-tree/abn_tree.css"]
}, {
    name: "angularFileUpload",
    module: !0,
    files: ["vendor/modules/angular-file-upload/angular-file-upload.min.js"]
}, {
    name: "ngImgCrop",
    module: !0,
    files: ["vendor/modules/ngImgCrop/ng-img-crop.js", "vendor/modules/ngImgCrop/ng-img-crop.css"]
}, {
    name: "smart-table",
    module: !0,
    files: ["vendor/modules/angular-smart-table/smart-table.min.js"]
}, {
    name: "easyPieChart",
    module: !1,
    files: ["vendor/jquery/easypiechart/jquery.easy-pie-chart.js"]
}, {
    name: "sparkline",
    module: !1,
    files: ["vendor/jquery/sparkline/jquery.sparkline.min.js"]
}, {
    name: "plot",
    module: !1,
    files: ["vendor/jquery/flot/jquery.flot.min.js", "vendor/jquery/flot/jquery.flot.resize.js", "vendor/jquery/flot/jquery.flot.tooltip.min.js", "vendor/jquery/flot/jquery.flot.spline.js", "vendor/jquery/flot/jquery.flot.orderBars.js", "vendor/jquery/flot/jquery.flot.pie.min.js"]
}, {
    name: "slimScroll",
    module: !1,
    files: ["vendor/jquery/slimscroll/jquery.slimscroll.min.js"]
}, {
    name: "vectorMap",
    module: !1,
    files: ["vendor/jquery/jvectormap/jquery-jvectormap.min.js", "vendor/jquery/jvectormap/jquery-jvectormap-world-mill-en.js", "vendor/jquery/jvectormap/jquery-jvectormap-us-aea-en.js", "vendor/jquery/jvectormap/jquery-jvectormap.css"]
}]).config(["$ocLazyLoadProvider", "MODULE_CONFIG", function(a, b) {
    a.config({
        debug: !1,
        events: !1,
        modules: b
    })
}]), angular.module("app").run(["$rootScope", "$state", "$stateParams", function(a, b, c) {
    a.$state = b, a.$stateParams = c
}]).config(["$stateProvider", "$urlRouterProvider", function(a, b) {
    b.otherwise("/app/dashboard"), a.state("app", {
        "abstract": !0,
        url: "/app",
        views: {
            "": {
                templateUrl: "resource/Heymtro/views/layout.html"
            },
            aside: {
                templateUrl: "resource/Heymtro/views/partials/aside.nav.uikit.html"
            }
        }
    }).state("app.dashboard", {
        url: "/dashboard",
        templateUrl: "resource/Heymtro/views/pages/dashboard.html",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load(["resource/Heymtro/scripts/controllers/chart.js", "resource/Heymtro/scripts/controllers/vectormap.js"])
            }]
        }
    }).state("mail", {
        url: "/mail",
        views: {
            "": {
                templateUrl: "views/layout.html"
            },
            aside: {
                templateUrl: "views/partials/aside.nav.mail.html"
            }
        }
    }).state("mail.inbox", {
        url: "/inbox",
        templateUrl: "views/pages/mail.html"
    })
}]), angular.module("app").config(["$stateProvider", "$urlRouterProvider", function(a) {
    a.state("page", {
        url: "/page",
        views: {
            "": {
                templateUrl: "views/layout.html"
            },
            aside: {
                templateUrl: "views/partials/aside.nav.pages.html"
            }
        }
    }).state("page.profile", {
        url: "/profile",
        templateUrl: "views/pages/profile.html"
    }).state("page.settings", {
        url: "/settings",
        templateUrl: "views/pages/settings.html"
    }).state("page.blank", {
        url: "/blank",
        templateUrl: "views/pages/blank.html"
    }).state("page.document", {
        url: "/document",
        templateUrl: "views/pages/document.html"
    }).state("signin", {
        url: "/signin",
        templateUrl: "views/pages/signin.html"
    }).state("signup", {
        url: "/signup",
        templateUrl: "views/pages/signup.html"
    }).state("forgot-password", {
        url: "/forgot-password",
        templateUrl: "views/pages/forgot-password.html"
    }).state("lockme", {
        url: "/lockme",
        templateUrl: "views/pages/lockme.html"
    }).state("404", {
        url: "/404",
        templateUrl: "views/pages/404.html"
    }).state("505", {
        url: "/505",
        templateUrl: "views/pages/505.html"
    })
}]), angular.module("app").config(["$stateProvider", "$urlRouterProvider", function(a) {
    a.state("ui", {
        url: "/ui",
        views: {
            aside: {
                templateUrl: "views/partials/aside.nav.uikit.html"
            },
            "": {
                templateUrl: "views/layout.html"
            }
        }
    }).state("ui.component", {
        url: "/component",
        template: "<div ui-view></div>"
    }).state("ui.component.arrow", {
        url: "/arrow",
        templateUrl: "views/ui/component/arrow.html"
    }).state("ui.component.badge-label", {
        url: "/badge-label",
        templateUrl: "views/ui/component/badge-label.html"
    }).state("ui.component.button", {
        url: "/button",
        templateUrl: "views/ui/component/button.html"
    }).state("ui.component.color", {
        url: "/color",
        templateUrl: "views/ui/component/color.html"
    }).state("ui.component.grid", {
        url: "/grid",
        templateUrl: "views/ui/component/grid.html"
    }).state("ui.component.icon", {
        url: "/icons",
        templateUrl: "views/ui/component/icon.html"
    }).state("ui.component.list", {
        url: "/list",
        templateUrl: "views/ui/component/list.html"
    }).state("ui.component.nav", {
        url: "/nav",
        templateUrl: "views/ui/component/nav.html"
    }).state("ui.component.panel", {
        url: "/panel",
        templateUrl: "views/ui/component/panel.html"
    }).state("ui.component.progressbar", {
        url: "/progressbar",
        templateUrl: "views/ui/component/progressbar.html"
    }).state("ui.component.streamline", {
        url: "/streamline",
        templateUrl: "views/ui/component/streamline.html"
    }).state("ui.component.timeline", {
        url: "/timeline",
        templateUrl: "views/ui/component/timeline.html"
    }).state("ui.angular-strap", {
        url: "/angular-strap",
        template: '<div ui-view class="fade-in"></div>',
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load(["scripts/controllers/angular-strap.js"])
            }]
        }
    }).state("ui.angular-strap.affix", {
        url: "/affix",
        templateUrl: "views/ui/angular-strap/affix.html"
    }).state("ui.angular-strap.alert", {
        url: "/alert",
        templateUrl: "views/ui/angular-strap/alert.html"
    }).state("ui.angular-strap.aside", {
        url: "/aside",
        templateUrl: "views/ui/angular-strap/aside.html"
    }).state("ui.angular-strap.button", {
        url: "/button",
        templateUrl: "views/ui/angular-strap/button.html"
    }).state("ui.angular-strap.collapse", {
        url: "/collapse",
        templateUrl: "views/ui/angular-strap/collapse.html"
    }).state("ui.angular-strap.dropdown", {
        url: "/dropdown",
        templateUrl: "views/ui/angular-strap/dropdown.html"
    }).state("ui.angular-strap.datepicker", {
        url: "/datepicker",
        templateUrl: "views/ui/angular-strap/datepicker.html"
    }).state("ui.angular-strap.timepicker", {
        url: "/timepicker",
        templateUrl: "views/ui/angular-strap/timepicker.html"
    }).state("ui.angular-strap.modal", {
        url: "/modal",
        templateUrl: "views/ui/angular-strap/modal.html"
    }).state("ui.angular-strap.select", {
        url: "/select",
        templateUrl: "views/ui/angular-strap/select.html"
    }).state("ui.angular-strap.tab", {
        url: "/tab",
        templateUrl: "views/ui/angular-strap/tab.html"
    }).state("ui.angular-strap.tooltip", {
        url: "/tooltip",
        templateUrl: "views/ui/angular-strap/tooltip.html"
    }).state("ui.angular-strap.popover", {
        url: "/popover",
        templateUrl: "views/ui/angular-strap/popover.html"
    }).state("ui.angular-strap.typeahead", {
        url: "/typehead",
        templateUrl: "views/ui/angular-strap/typeahead.html"
    }).state("ui.form", {
        url: "/form",
        template: "<div ui-view></div>"
    }).state("ui.form.layout", {
        url: "/layout",
        templateUrl: "views/ui/form/layout.html"
    }).state("ui.form.element", {
        url: "/element",
        templateUrl: "views/ui/form/element.html"
    }).state("ui.form.validation", {
        url: "/validation",
        templateUrl: "views/ui/form/validation.html"
    }).state("ui.form.select", {
        url: "/select",
        templateUrl: "views/ui/form/select.html",
        controller: "SelectCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("scripts/controllers/select.js")
            }]
        }
    }).state("ui.form.editor", {
        url: "/editor",
        templateUrl: "views/ui/form/editor.html",
        controller: "EditorCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("scripts/controllers/editor.js")
            }]
        }
    }).state("ui.form.slider", {
        url: "/slider",
        templateUrl: "views/ui/form/slider.html",
        controller: "SliderCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("scripts/controllers/slider.js")
            }]
        }
    }).state("ui.form.tree", {
        url: "/tree",
        templateUrl: "views/ui/form/tree.html",
        controller: "TreeCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("scripts/controllers/tree.js")
            }]
        }
    }).state("ui.form.file-upload", {
        url: "/file-upload",
        templateUrl: "views/ui/form/file-upload.html",
        controller: "UploadCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("angularFileUpload").then(function() {
                    return a.load("scripts/controllers/upload.js")
                })
            }]
        }
    }).state("ui.form.image-crop", {
        url: "/image-crop",
        templateUrl: "views/ui/form/image-crop.html",
        controller: "ImgCropCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("ngImgCrop").then(function() {
                    return a.load("scripts/controllers/imgcrop.js")
                })
            }]
        }
    }).state("ui.table", {
        url: "/table",
        template: "<div ui-view></div>"
    }).state("ui.table.static", {
        url: "/static",
        templateUrl: "views/ui/table/static.html"
    }).state("ui.table.smart", {
        url: "/smart",
        templateUrl: "views/ui/table/smart.html",
        controller: "TableCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("smart-table").then(function() {
                    return a.load("scripts/controllers/table.js")
                })
            }]
        }
    }).state("ui.chart", {
        url: "/chart",
        templateUrl: "views/ui/chart/chart.html",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("scripts/controllers/chart.js")
            }]
        }
    }).state("ui.map", {
        url: "/map",
        template: "<div ui-view></div>"
    }).state("ui.map.google", {
        url: "/google",
        templateUrl: "views/ui/map/google.html",
        controller: "GoogleMapCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load([{
                    files: ["vendor/jquery/load-google-maps.js", "scripts/controllers/googlemap.js"]
                }, {
                    name: "ui.map",
                    files: ["vendor/modules/angular-ui-map/ui-map.js"]
                }]).then(function() {
                    return loadGoogleMaps()
                })
            }]
        }
    }).state("ui.map.vector", {
        url: "/vector",
        templateUrl: "views/ui/map/vector.html",
        controller: "VectorMapCtrl",
        resolve: {
            deps: ["$ocLazyLoad", function(a) {
                return a.load("scripts/controllers/vectormap.js")
            }]
        }
    })
}]), angular.module("app").controller("MainCtrl", ["$scope", "$translate", "$localStorage", "$window", function(a, b, c, d) {
    function e(a) {
        var b = a.navigator.userAgent || a.navigator.vendor || a.opera;
        return /iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/.test(b)
    }
    var f = !!navigator.userAgent.match(/MSIE/i) || !!navigator.userAgent.match(/Trident.*rv:11\./);
    f && angular.element(d.document.body).addClass("ie"), e(d) && angular.element(d.document.body).addClass("smart"), a.app = {
        name: "HeyMetro",
        version: "1.2",
        color: {
            primary: "#155abb",
            info: "#2772ee",
            success: "#4bb622",
            warning: "#f88311",
            danger: "#e11144",
            inverse: "#a66bee",
            light: "#f1f2f3",
            dark: "#202a3a"
        },
        settings: {
            headerColor: "bg-primary",
            headerFixed: !0,
            headerShadow: !0,
            asideColor: "bg-dark lt",
            asideTop: !0
        }
    }, a.options = {
        headerColor: ["bg-primary lt", "bg-primary ", "bg-primary dk", "bg-info lt", "bg-info", "bg-info dk", "bg-success lt", "bg-success ", "bg-success dk", "bg-inverse lt", "bg-inverse ", "bg-inverse dk", "bg-dark lt", "bg-dark", "bg-dark dk ", "bg-black ", "bg-black dk", "bg-white box-shadow-md"],
        asideColor: ["bg-primary dk", "bg-info dk", "bg-success dk", "bg-dark lt", "bg-dark", "bg-dark dk", "bg-black lt", "bg-black", "bg-black dk", "bg-white", "bg-light", "bg-light dk"]
    }, a.setHeaderColor = function(b) {
        a.app.settings.headerColor = b
    }, a.setAsideColor = function(b) {
        a.app.settings.asideColor = b
    }, angular.isDefined(c.appSettings) ? a.app.settings = c.appSettings : c.appSettings = a.app.settings, a.$watch("app.settings", function() {
        c.appSettings = a.app.settings
    }, !0), a.langs = {
        en: "English",
        zh_CN: "中文"
    }, a.selectLang = a.langs[b.proposedLanguage()] || "English", a.setLang = function(c) {
        a.selectLang = a.langs[c], b.use(c)
    }
}]), angular.module("app").directive("lazyLoad", ["MODULE_CONFIG", "$ocLazyLoad", "$compile", function(a, b, c) {
    return {
        restrict: "A",
        compile: function(d) {
            var e, f = d.contents().remove();
            return function(d, g, h) {
                angular.forEach(a, function(a) {
                    a.name == h.lazyLoad && (e = a.module ? a.name : a.files)
                }), b.load(e).then(function() {
                    c(f)(d, function(a) {
                        g.append(a)
                    })
                })
            }
        }
    }
}]), angular.module("app").directive("uiFullscreen", ["$ocLazyLoad", "$document", function(a, b) {
    return {
        restrict: "AC",
        link: function(c, d, e) {
            d.addClass("hide"), a.load("vendor/libs/screenfull.min.js").then(function() {
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
}]), angular.module("ui.jp", ["oc.lazyLoad", "ui.load"]).value("uiJpConfig", {}).directive("uiJp", ["uiJpConfig", "MODULE_CONFIG", "$ocLazyLoad", "uiLoad", "$timeout", function(a, b, c, d, e) {
    return {
        restrict: "A",
        compile: function(c, f) {
            var g = a && a[f.uiJp];
            return function(a, c, f) {
                function h() {
                    var b = [];
                    return f.uiOptions ? (b = a.$eval("[" + f.uiOptions + "]"), angular.isObject(g) && angular.isObject(b[0]) && (b[0] = angular.extend({}, g, b[0]))) : g && (b = [g]), b
                }

                function i() {
                    e(function() {
                        c[f.uiJp].apply(c, h())
                    }, 0, !1)
                }

                function j() {
                    f.uiRefresh && a.$watch(f.uiRefresh, function() {
                        i()
                    })
                }
                f.ngModel && c.is("select,input,textarea") && c.bind("change", function() {
                    c.trigger("input")
                });
                var k = !1;
                angular.forEach(b, function(a) {
                    a.name == f.uiJp && (k = a.files)
                }), k ? d.load(k).then(function() {
                    i(), j()
                }).catch(function() {}) : (i(), j())
            }
        }
    }
}]), angular.module("app").directive("uiNav", ["$timeout", function() {
    return {
        restrict: "AC",
        link: function(a, b) {
            b.find("a").bind("click", function() {
                var b = angular.element(this).parent();
                b.parent().find("li").removeClass("active"), b.toggleClass("active"), b.find("ul") && (a.app.asideCollapse = !1)
            })
        }
    }
}]), angular.module("app").directive("uiScroll", ["$location", "$anchorScroll", function(a, b) {
    return {
        restrict: "AC",
        replace: !0,
        link: function(c, d, e) {
            d.bind("click", function() {
                a.hash(e.uiScroll), b()
            })
        }
    }
}]), angular.module("ui.load", []).service("uiLoad", ["$document", "$q", "$timeout", function(a, b, c) {
    var d = [],
        e = !1,
        f = b.defer();
    this.load = function(a) {
        a = angular.isArray(a) ? a : a.split(/\s+/);
        var b = this;
        return e || (e = f.promise), angular.forEach(a, function(a) {
            e = e.then(function() {
                return a.indexOf(".css") >= 0 ? b.loadCSS(a) : b.loadScript(a)
            })
        }), f.resolve(), e
    }, this.loadScript = function(e) {
        if (d[e]) return d[e].promise;
        var f = b.defer(),
            g = a[0].createElement("script");
        return g.src = e, g.onload = function(a) {
            c(function() {
                f.resolve(a)
            })
        }, g.onerror = function(a) {
            c(function() {
                f.reject(a)
            })
        }, a[0].body.appendChild(g), d[e] = f, f.promise
    }, this.loadCSS = function(e) {
        if (d[e]) return d[e].promise;
        var f = b.defer(),
            g = a[0].createElement("link");
        return g.rel = "stylesheet", g.type = "text/css", g.href = e, g.onload = function(a) {
            c(function() {
                f.resolve(a)
            })
        }, g.onerror = function(a) {
            c(function() {
                f.reject(a)
            })
        }, a[0].head.appendChild(g), d[e] = f, f.promise
    }
}]), angular.module("app").filter("fromNow", function() {
    return function(a) {
        return moment(a).fromNow()
    }
});