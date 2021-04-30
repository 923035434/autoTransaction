﻿/** layui-v2.5.6 MIT License By https://www.layui.com */
layui.define("jquery", function (r) {
    var c = layui.$, h = layui.hint(), l = layui.device(), g = "element", f = "layui-this", b = "layui-show",
        j = function () {
            this.config = {}
        };
    j.prototype.set = function (t) {
        var u = this;
        c.extend(true, u.config, t);
        return u
    };
    j.prototype.on = function (t, u) {
        return layui.onevent.call(this, g, t, u)
    };
    j.prototype.tabAdd = function (w, v) {
        var y = ".layui-tab-title", A = c(".layui-tab[lay-filter=" + w + "]"), u = A.children(y),
            z = u.children(".layui-tab-bar"), x = A.children(".layui-tab-content"),
            t = '<li lay-id="' + (v.id || "") + '"' + (v.attr ? ' lay-attr="' + v.attr + '"' : "") + ">" + (v.title || "unnaming") + "</li>";
        z[0] ? z.before(t) : u.append(t);
        x.append('<div class="layui-tab-item">' + (v.content || "") + "</div>");
        p.hideTabMore(true);
        p.tabAuto();
        return this
    };
    j.prototype.tabDelete = function (v, u) {
        var w = ".layui-tab-title", y = c(".layui-tab[lay-filter=" + v + "]"), t = y.children(w),
            x = t.find('>li[lay-id="' + u + '"]');
        p.tabDelete(null, x);
        return this
    };
    j.prototype.tabChange = function (v, u) {
        var w = ".layui-tab-title", y = c(".layui-tab[lay-filter=" + v + "]"), t = y.children(w),
            x = t.find('>li[lay-id="' + u + '"]');
        p.tabClick.call(x[0], null, null, x);
        return this
    };
    j.prototype.tab = function (t) {
        t = t || {};
        m.on("click", t.headerElem, function (v) {
            var u = c(this).index();
            p.tabClick.call(this, v, u, null, t)
        })
    };
    j.prototype.progress = function (u, w) {
        var t = "layui-progress", v = c("." + t + "[lay-filter=" + u + "]"), y = v.find("." + t + "-bar"),
            x = y.find("." + t + "-text");
        y.css("width", w);
        x.text(w);
        return this
    };
    var q = ".layui-nav", s = "layui-nav-item", n = "layui-nav-bar", k = "layui-nav-tree", d = "layui-nav-child",
        e = "layui-nav-more", o = "layui-anim layui-anim-upbit", p = {
            tabClick: function (y, w, x, B) {
                B = B || {};
                var u = x || c(this), w = w || u.parent().children("li").index(u),
                    z = B.headerElem ? u.parent() : u.parents(".layui-tab").eq(0),
                    A = B.bodyElem ? c(B.bodyElem) : z.children(".layui-tab-content").children(".layui-tab-item"),
                    v = u.find("a"), t = z.attr("lay-filter");
                if (!(v.attr("href") !== "javascript:;" && v.attr("target") === "_blank")) {
                    u.addClass(f).siblings().removeClass(f);
                    A.eq(w).addClass(b).siblings().removeClass(b)
                }
                layui.event.call(this, g, "tab(" + t + ")", {elem: z, index: w})
            }, tabDelete: function (z, y) {
                var t = y || c(this).parent(), v = t.index(), u = t.parents(".layui-tab").eq(0),
                    x = u.children(".layui-tab-content").children(".layui-tab-item"), w = u.attr("lay-filter");
                if (t.hasClass(f)) {
                    if (t.next()[0]) {
                        p.tabClick.call(t.next()[0], null, v + 1)
                    } else {
                        if (t.prev()[0]) {
                            p.tabClick.call(t.prev()[0], null, v - 1)
                        }
                    }
                }
                t.remove();
                x.eq(v).remove();
                setTimeout(function () {
                    p.tabAuto()
                }, 50);
                layui.event.call(this, g, "tabDelete(" + w + ")", {elem: u, index: v})
            }, tabAuto: function () {
                var x = "layui-tab-scroll", w = "layui-tab-more", t = "layui-tab-bar", v = "layui-tab-close", u = this;
                c(".layui-tab").each(function () {
                    var A = c(this), C = A.children(".layui-tab-title"),
                        z = A.children(".layui-tab-content").children(".layui-tab-item"), B = 'lay-stope="tabmore"',
                        y = c('<span class="layui-unselect layui-tab-bar" ' + B + "><i " + B + ' class="layui-icon">&#xe61a;</i></span>');
                    if (u === window && l.ie != 8) {
                        p.hideTabMore(true)
                    }
                    if (A.attr("lay-allowClose")) {
                        C.find("li").each(function () {
                            var D = c(this);
                            if (!D.find("." + v)[0]) {
                                var E = c('<i class="layui-icon layui-unselect ' + v + '">&#x1006;</i>');
                                E.on("click", p.tabDelete);
                                D.append(E)
                            }
                        })
                    }
                    if (typeof A.attr("lay-unauto") === "string") {
                        return
                    }
                    if (C.prop("scrollWidth") > C.outerWidth() + 1) {
                        if (C.find("." + t)[0]) {
                            return
                        }
                        C.append(y);
                        A.attr("overflow", "");
                        y.on("click", function (D) {
                            C[this.title ? "removeClass" : "addClass"](w);
                            this.title = this.title ? "" : "收缩"
                        })
                    } else {
                        C.find("." + t).remove();
                        A.removeAttr("overflow")
                    }
                })
            }, hideTabMore: function (u) {
                var t = c(".layui-tab-title");
                if (u === true || c(u.target).attr("lay-stope") !== "tabmore") {
                    t.removeClass("layui-tab-more");
                    t.find(".layui-tab-bar").attr("title", "")
                }
            }, clickThis: function () {
                var x = c(this), u = x.parents(q), w = u.attr("lay-filter"), v = x.parent(), z = x.siblings("." + d),
                    t = typeof v.attr("lay-unselect") === "string";
                if (!(x.attr("href") !== "javascript:;" && x.attr("target") === "_blank") && !t) {
                    if (!z[0]) {
                        u.find("." + f).removeClass(f);
                        v.addClass(f)
                    }
                }
                if (u.hasClass(k)) {
                    z.removeClass(o);
                    if (z[0]) {
                        if (v.hasClass("admin-nav-hover")) {
                            return
                        }
                        var y = this;
                        if (!v.hasClass("layui-nav-itemed")) {
                            if (u.attr("lay-shrink") === "all") {
                                v.parent().children(".layui-nav-itemed").not(v).children(".layui-nav-child").slideUp("fast");
                                v.parent().children(".layui-nav-itemed").not(v).removeClass(s + "ed")
                            }
                            v.children(".layui-nav-child").slideDown("fast", function () {
                                layui.event.call(y, g, "nav(" + w + ")", x);
                                v.trigger("mouseenter")
                            });
                            v["addClass"](s + "ed")
                        } else {
                            v.children(".layui-nav-child").slideUp("fast", function () {
                                layui.event.call(y, g, "nav(" + w + ")", x);
                                v.trigger("mouseenter")
                            });
                            v["removeClass"](s + "ed")
                        }
                    } else {
                        layui.event.call(this, g, "nav(" + w + ")", x)
                    }
                } else {
                    layui.event.call(this, g, "nav(" + w + ")", x)
                }
            }, collapse: function () {
                var v = c(this), z = v.find(".layui-colla-icon"), w = v.siblings(".layui-colla-content"),
                    A = v.parents(".layui-collapse").eq(0), u = A.attr("lay-filter"), y = w.css("display") === "none";
                if (typeof A.attr("lay-accordion") === "string") {
                    var B = A.children(".layui-colla-item").children("." + b).not(w);
                    if (x()) {
                        B.siblings(".layui-colla-title").children(".layui-colla-icon").html("&#xe602;")
                    } else {
                        B.siblings(".layui-colla-title").children(".layui-colla-icon").html("&#xe602;");
                        B.siblings(".layui-colla-title").children(".layui-colla-icon").css({
                            "transition": "all .3s",
                            "transform": "rotate(0deg)"
                        })
                    }
                    B.slideUp("fast", function () {
                        c(this).removeAttr("style");
                        B.removeClass(b)
                    })
                }
                if (y) {
                    w.slideDown("fast", function () {
                        c(this).removeAttr("style");
                        w["addClass"](b)
                    })
                } else {
                    w.slideUp("fast", function () {
                        c(this).removeAttr("style");
                        w["removeClass"](b)
                    })
                }
                if (x()) {
                    z.html(y ? "&#xe61a;" : "&#xe602;")
                } else {
                    z.html("&#xe602;");
                    var t = y ? "90deg" : "0deg";
                    z.css({"transition": "all .3s", "transform": "rotate(" + t + ")"})
                }
                layui.event.call(this, g, "collapse(" + u + ")", {title: v, content: w, show: y});

                function x() {
                    return layui.device().ie == 8
                }
            }
        };
    j.prototype.init = function (v, u) {
        var x = this, w = function () {
            return u ? ('[lay-filter="' + u + '"]') : ""
        }(), t = {
            tab: function () {
                p.tabAuto.call({})
            }, nav: function () {
                var A = 200, C = {}, B = {}, z = {}, y = function (E, G, D) {
                    var F = c(this), H = F.find("." + d);
                    if (G.hasClass(k)) {
                        E.css({top: F.position().top, height: F.children("a").outerHeight(), opacity: 1})
                    } else {
                        H.addClass(o);
                        E.css({
                            left: F.position().left + parseFloat(F.css("marginLeft")),
                            top: F.position().top + F.height() - E.height()
                        });
                        C[D] = setTimeout(function () {
                            E.css({width: F.width(), opacity: 1})
                        }, l.ie && l.ie < 10 ? 0 : A);
                        clearTimeout(z[D]);
                        if (H.css("display") === "block") {
                            clearTimeout(B[D])
                        }
                        B[D] = setTimeout(function () {
                            H.addClass(b);
                            F.find("." + e).addClass(e + "d")
                        }, 300)
                    }
                };
                c(q + w).each(function (D) {
                    var F = c(this), E = c('<span class="' + n + '"></span>'), G = F.find("." + s);
                    if (!F.find("." + n)[0]) {
                        F.append(E);
                        G.on("mouseenter", function () {
                            y.call(this, E, F, D)
                        }).on("mouseleave", function () {
                            if (!F.hasClass(k)) {
                                clearTimeout(B[D]);
                                B[D] = setTimeout(function () {
                                    F.find("." + d).removeClass(b);
                                    F.find("." + e).removeClass(e + "d")
                                }, 300)
                            }
                        });
                        F.on("mouseleave", function () {
                            clearTimeout(C[D]);
                            z[D] = setTimeout(function () {
                                if (F.hasClass(k)) {
                                    E.css({height: 0, top: E.position().top + E.height() / 2, opacity: 0})
                                } else {
                                    E.css({width: 0, left: E.position().left + E.width() / 2, opacity: 0})
                                }
                            }, A)
                        })
                    }
                    G.find("a").each(function () {
                        var I = c(this), H = I.parent(), J = I.siblings("." + d);
                        if (J[0] && !I.children("." + e)[0]) {
                            I.append('<span class="' + e + '"></span>')
                        }
                        I.off("click", p.clickThis).on("click", p.clickThis)
                    })
                })
            }, breadcrumb: function () {
                var y = ".layui-breadcrumb";
                c(y + w).each(function () {
                    var B = c(this), A = "lay-separator", C = B.attr(A) || "/", z = B.find("a");
                    if (z.next("span[" + A + "]")[0]) {
                        return
                    }
                    z.each(function (D) {
                        if (D === z.length - 1) {
                            return
                        }
                        c(this).after("<span " + A + ">" + C + "</span>")
                    });
                    B.css("visibility", "visible")
                })
            }, progress: function () {
                var y = "layui-progress";
                c("." + y + w).each(function () {
                    var A = c(this), B = A.find(".layui-progress-bar"), z = B.attr("lay-percent");
                    B.css("width", function () {
                        return /^.+\/.+$/.test(z) ? (new Function("return " + z)() * 100) + "%" : z
                    }());
                    if (A.attr("lay-showPercent")) {
                        setTimeout(function () {
                            B.html('<span class="' + y + '-text">' + z + "</span>")
                        }, 350)
                    }
                })
            }, collapse: function () {
                var y = "layui-collapse";
                c("." + y + w).each(function () {
                    var z = c(this).find(".layui-colla-item");
                    z.each(function () {
                        var D = c(this), C = D.find(".layui-colla-title"), A = D.find(".layui-colla-content"),
                            B = A.css("display") === "none";
                        C.find(".layui-colla-icon").remove();
                        C.append('<i class="layui-icon layui-colla-icon">' + (B ? "&#xe602;" : "&#xe61a;") + "</i>");
                        C.off("click", p.collapse).on("click", p.collapse)
                    })
                })
            }
        };
        return t[v] ? t[v]() : layui.each(t, function (y, z) {
            z()
        })
    };
    j.prototype.render = j.prototype.init;
    var a = new j(), m = c(document);
    a.render();
    var i = ".layui-tab-title li";
    m.on("click", i, p.tabClick);
    m.on("click", p.hideTabMore);
    c(window).on("resize", p.tabAuto);
    r(g, a)
});