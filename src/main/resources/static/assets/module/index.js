﻿/** EasyWeb iframe v3.1.7 date:2019-03-08 License By http://easyweb.vip */
layui.define(["layer", "element", "admin"], function (r) {
    var c = layui.jquery;
    var q = layui.layer;
    var b = layui.element;
    var k = layui.admin;
    var o = k.setter;
    var a = ".layui-layout-admin>.layui-header";
    var m = ".layui-layout-admin>.layui-side>.layui-side-scroll";
    var i = ".layui-layout-admin>.layui-body";
    var l = i + ">.layui-tab";
    var p = i + ">.layui-body-header";
    var h = "admin-pagetabs";
    var n = "admin-side-nav";
    var j = {};
    var e = false;
    var f = {homeUrl: undefined, mTabPosition: undefined, mTabList: []};
    f.loadView = function (y) {
        var u = y.menuName;
        var v = y.menuPath;
        if (!v) {
            return q.msg("url不能为空", {icon: 2, anim: 6})
        }
        if (o.pageTabs) {
            var t;
            c(l + ">.layui-tab-title>li").each(function () {
                if (y.id && y.id === c(this).attr("lay-id") || (c(this).attr("lay-id").split('?')[0] === v)) {
                    t = true;
                    v = c(this).attr("lay-id");
                    return false
                }
                if (c(this).attr("lay-id") === v) {
                    t = true;
                    return false
                }
            });
            if (!t) {
                if ((f.mTabList.length + 1) >= o.maxTabNum) {
                    q.msg("最多打开" + o.maxTabNum + "个选项卡", {icon: 2, anim: 6});
                    k.activeNav(f.mTabPosition);
                    return
                }
                e = true;
                b.tabAdd(h, {
                    id: !y.id ? v : y.id,
                    title: '<span class="title">' + (u ? u : "") + "</span>",
                    content: '<iframe lay-id="' + v + '" src="' + v + '" frameborder="0" class="admin-iframe"></iframe>'
                });
                if (v !== f.homeUrl) {
                    f.mTabList.push({
                        menuName: y.menuName,
                        menuPath: !y.id ? y.menuPath : y.id
                    })
                }
                if (o.cacheTab) {
                    k.putTempData("indexTabs", f.mTabList)
                }
            }
            b.tabChange(h, !y.id ? v : y.id);
            if (y.id || y.refresh) {
                //刷新页面（指带了id属性需要传参数的页面）
                var selectId = $(parent.document).find(".layui-tab-title .layui-this").attr("lay-id");
                $(parent.document).find("iframe[lay-id='" + selectId + "']").attr("src", y.menuPath);
            }
        } else {
            k.activeNav(v);
            var s = c(i + ">div>.admin-iframe");
            if (s.length <= 0) {
                var x = '<div class="layui-body-header">';
                x += '      <span class="layui-body-header-title"></span>';
                x += '      <span class="layui-breadcrumb pull-right" lay-filter="admin-body-breadcrumb" style="visibility: visible;"></span>';
                x += "   </div>";
                x += '   <div style="-webkit-overflow-scrolling: touch;">';
                x += '      <iframe lay-id="' + v + '" src="' + v + '" frameborder="0" class="admin-iframe"></iframe>';
                x += "   </div>";
                c(i).html(x)
            } else {
                s.attr("lay-id", v);
                s.attr("src", v)
            }
            c('[lay-filter="admin-body-breadcrumb"]').html(f.getBreadcrumbHtml(v));
            var w = u;
            if (v === f.homeUrl) {
                w = c(u).text() || c(m + ' [lay-href="' + f.homeUrl + '"]').text() || "主页"
            }
            f.setTabTitle(w);
            f.mTabList.splice(0, f.mTabList.length);
            if (v !== f.homeUrl) {
                f.mTabList.push(y);
                f.mTabPosition = v
            } else {
                f.mTabPosition = undefined
            }
            if (o.cacheTab) {
                k.putTempData("indexTabs", f.mTabList);
                k.putTempData("tabPosition", f.mTabPosition)
            }
        }
        if (k.getPageWidth() <= 768) {
            k.flexible(true)
        }
    };
    f.loadHome = function (w) {
        var v = k.getTempData("indexTabs");
        var u = k.getTempData("tabPosition");
        f.homeUrl = w.menuPath;
        f.loadView(w);
        if (!o.pageTabs) {
            k.activeNav(f.homeUrl)
        }
        if (w.loadSetting === undefined || w.loadSetting) {
            if (o.cacheTab && v) {
                var s = undefined;
                for (var t = 0; t < v.length; t++) {
                    if (o.pageTabs && !w.onlyLast) {
                        f.loadView(v[t])
                    }
                    if (v[t].menuPath === u) {
                        s = t
                    }
                }
                if (s !== undefined) {
                    setTimeout(function () {
                        f.loadView(v[s]);
                        if (!o.pageTabs) {
                            k.activeNav(u)
                        }
                    }, 150)
                }
            }
        }
    };
    f.openTab = function (s) {
        if (window !== top && !k.isTop() && top.layui && top.layui.index) {
            return top.layui.index.openTab(s)
        }
        if (s.end) {
            j[s.url] = s.end
        }
        f.loadView({menuPath: s.url, menuName: s.title, id: s.id, refresh: s.refresh})
    };
    f.closeTab = function (s) {
        if (window !== top && !k.isTop() && top.layui && top.layui.index) {
            return top.layui.index.closeTab(s)
        }
        b.tabDelete(h, s)
    };
    f.setTabCache = function (s) {
        if (window !== top && !k.isTop() && top.layui && top.layui.index) {
            return top.layui.index.setTabCache(s)
        }
        k.putSetting("cacheTab", s);
        if (s) {
            k.putTempData("indexTabs", f.mTabList);
            k.putTempData("tabPosition", f.mTabPosition)
        } else {
            f.clearTabCache()
        }
    };
    f.clearTabCache = function () {
        k.putTempData("indexTabs", null);
        k.putTempData("tabPosition", null)
    };
    f.setTabTitle = function (t, s) {
        if (window !== top && !k.isTop() && top.layui && top.layui.index) {
            return top.layui.index.setTabTitle(t, s)
        }
        if (o.pageTabs) {
            if (!s) {
                s = c(l + ">.layui-tab-title>li.layui-this").attr("lay-id")
            }
            if (s) {
                c(l + '>.layui-tab-title>li[lay-id="' + s + '"] .title').html(t || "")
            }
        } else {
            if (t) {
                c(p + ">.layui-body-header-title").html(t);
                c(p).addClass("show");
                c(a).css("box-shadow", "0 1px 0 0 rgba(0, 0, 0, .03)")
            } else {
                c(p).removeClass("show");
                k.util.removeStyle(a, "box-shadow")
            }
        }
    };
    f.setTabTitleHtml = function (s) {
        if (window !== top && !k.isTop() && top.layui && top.layui.index) {
            return top.layui.index.setTabTitleHtml(s)
        }
        if (!o.pageTabs) {
            if (s) {
                c(p).addClass("show");
                c(p).html(s)
            } else {
                c(p).removeClass("show")
            }
        }
    };
    f.getBreadcrumb = function (s) {
        if (!s) {
            s = c(i + ">div>.admin-iframe").attr("lay-id")
        }
        var u = [];
        var t = c(m).find('[lay-href="' + s + '"]');
        if (t) {
            u.push(t.text().replace(/(^\s*)|(\s*$)/g, ""))
        }
        while (true) {
            t = t.parent("dd").parent("dl").prev("a");
            if (t.length === 0) {
                break
            }
            u.unshift(t.text().replace(/(^\s*)|(\s*$)/g, ""))
        }
        return u
    };
    f.getBreadcrumbHtml = function (s) {
        var v = f.getBreadcrumb(s);
        var u = (s === f.homeUrl ? "" : ('<a ew-href="' + f.homeUrl + '">工作台</a>'));
        for (var t = 0; t < v.length - 1; t++) {
            if (u) {
                u += '<span lay-separator="">/</span>'
            }
            u += ("<a><cite>" + v[t] + "</cite></a>")
        }
        return u
    };
    var g = ".layui-layout-admin .site-mobile-shade";
    if (c(g).length <= 0) {
        c(".layui-layout-admin").append('<div class="site-mobile-shade"></div>')
    }
    c(g).click(function () {
        k.flexible(true)
    });
    if (o.pageTabs && c(l).length === 0) {
        var d = '<div class="layui-tab" lay-allowClose="true" lay-filter="' + h + '">';
        d += '      <ul class="layui-tab-title"></ul>';
        d += '      <div class="layui-tab-content"></div>';
        d += "   </div>";
        d += '   <div class="layui-icon admin-tabs-control layui-icon-prev" ew-event="leftPage"></div>';
        d += '   <div class="layui-icon admin-tabs-control layui-icon-next" ew-event="rightPage"></div>';
        d += '   <div class="layui-icon admin-tabs-control layui-icon-down">';
        d += '      <ul class="layui-nav" lay-filter="admin-pagetabs-nav">';
        d += '         <li class="layui-nav-item" lay-unselect>';
        d += '            <dl class="layui-nav-child layui-anim-fadein">';
        d += '               <dd ew-event="closeThisTabs" lay-unselect><a>关闭当前标签页</a></dd>';
        d += '               <dd ew-event="closeOtherTabs" lay-unselect><a>关闭其它标签页</a></dd>';
        d += '               <dd ew-event="closeAllTabs" lay-unselect><a>关闭全部标签页</a></dd>';
        d += "            </dl>";
        d += "         </li>";
        d += "      </ul>";
        d += "   </div>";
        c(i).html(d);
        b.render("nav", "admin-pagetabs-nav")
    }
    if (o.pageTabs && o.tabAutoRefresh) {
        c(l).attr("lay-autoRefresh", "true")
    }
    b.on("nav(" + n + ")", function (v) {
        var u = c(v);
        var s = u.attr("lay-href");
        if (s && s !== "javascript:;") {
            var t = u.attr("ew-title");
            if (!t) {
                t = u.text().replace(/(^\s*)|(\s*$)/g, "")
            }
            f.loadView({menuPath: s, menuName: t})
        }
    });
    b.on("tab(" + h + ")", function () {
        var t = c(this).attr("lay-id");
        f.mTabPosition = (t !== f.homeUrl ? t : undefined);
        if (o.cacheTab) {
            k.putTempData("tabPosition", f.mTabPosition)
        }
        k.activeNav(t);
        k.rollPage("auto");
        var s = c(l).attr("lay-autoRefresh");
        if (s === "true" && !e) {
            k.refresh(t)
        }
        e = false
    });
    b.on("tabDelete(" + h + ")", function (u) {
        var s = f.mTabList[u.index - 1];
        if (s) {
            var t = s.menuPath;
            f.mTabList.splice(u.index - 1, 1);
            if (o.cacheTab) {
                k.putTempData("indexTabs", f.mTabList)
            }
            j[t] && j[t].call()
        }
        if (c(l + ">.layui-tab-title>li.layui-this").length <= 0) {
            c(l + ">.layui-tab-title>li:last").trigger("click")
        }
    });
    c(document).off("click.navMore").on("click.navMore", "[nav-bind]", function () {
        var s = c(this).attr("nav-bind");
        c('ul[lay-filter="' + n + '"]').addClass("layui-hide");
        c('ul[nav-id="' + s + '"]').removeClass("layui-hide");
        c(a + ">.layui-nav .layui-nav-item").removeClass("layui-this");
        c(this).parent(".layui-nav-item").addClass("layui-this");
        if (k.getPageWidth() <= 768) {
            k.flexible(false)
        }
    });
    if (o.openTabCtxMenu && o.pageTabs) {
        layui.use("contextMenu", function () {
            var s = layui.contextMenu;
            if (!s) {
                return
            }
            c(l + ">.layui-tab-title").off("contextmenu.tab").on("contextmenu.tab", "li", function (u) {
                var t = c(this).attr("lay-id");
                s.show([{
                    icon: "layui-icon layui-icon-refresh", name: "刷新当前", click: function () {
                        b.tabChange(h, t);
                        var v = c(l).attr("lay-autoRefresh");
                        if (!v || v !== "true") {
                            k.refresh(t)
                        }
                    }
                }, {
                    icon: "layui-icon layui-icon-close-fill ctx-ic-lg", name: "关闭当前", click: function () {
                        k.closeThisTabs(t)
                    }
                }, {
                    icon: "layui-icon layui-icon-unlink", name: "关闭其他", click: function () {
                        k.closeOtherTabs(t)
                    }
                }, {
                    icon: "layui-icon layui-icon-close ctx-ic-lg", name: "关闭全部", click: function () {
                        k.closeAllTabs()
                    }
                }], u.clientX, u.clientY);
                return false
            })
        })
    }
    r("index", f)
});