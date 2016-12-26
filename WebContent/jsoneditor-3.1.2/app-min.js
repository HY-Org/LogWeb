function QueryParams() {}
function Notify() {
    this.dom = {};
    var e = this;
    util.addEventListener(document, "keydown", function (t) {
        e.onKeyDown(t)
    })
}
function Splitter(e) {
    if (!e || !e.container) throw new Error("params.container undefined in Splitter constructor");
    var t = this;
    util.addEventListener(e.container, "mousedown", function (e) {
        t.onMouseDown(e)
    }), this.container = e.container, this.snap = Number(e.snap) || 200, this.width = void 0, this.value = void 0, this.onChange = e.change ? e.change : function () {}, this.params = {}
}
var util = {};
util.parse = function (e) {
    try {
        return JSON.parse(e)
    } catch (t) {
        throw util.validate(e), t
    }
}, util.validate = function (e) {
    "undefined" != typeof jsonlint ? jsonlint.parse(e) : JSON.parse(e)
}, util.addClassName = function (e, t) {
    var o = e.className.split(" "); - 1 == o.indexOf(t) && (o.push(t), e.className = o.join(" "))
}, util.removeClassName = function (e, t) {
    var o = e.className.split(" "),
        i = o.indexOf(t); - 1 != i && (o.splice(i, 1), e.className = o.join(" "))
}, util.getInternetExplorerVersion = function () {
    if (-1 == _ieVersion) {
        var e = -1;
        if ("Microsoft Internet Explorer" == navigator.appName) {
            var t = navigator.userAgent,
                o = new RegExp("MSIE ([0-9]{1,}[.0-9]{0,})");
            null != o.exec(t) && (e = parseFloat(RegExp.$1))
        }
        _ieVersion = e
    }
    return _ieVersion
}, util.isFirefox = function () {
    return -1 != navigator.userAgent.indexOf("Firefox")
};
var _ieVersion = -1;
util.addEventListener = function (e, t, o, i) {
    if (e.addEventListener) return void 0 === i && (i = !1), "mousewheel" === t && util.isFirefox() && (t = "DOMMouseScroll"), e.addEventListener(t, o, i), o;
    if (e.attachEvent) {
        var n = function () {
            return o.call(e, window.event)
        };
        return e.attachEvent("on" + t, n), n
    }
}, util.removeEventListener = function (e, t, o, i) {
    e.removeEventListener ? (void 0 === i && (i = !1), "mousewheel" === t && util.isFirefox() && (t = "DOMMouseScroll"), e.removeEventListener(t, o, i)) : e.detachEvent && e.detachEvent("on" + t, o)
}, QueryParams.prototype.getQuery = function () {
    for (var e = window.location.search.substring(1), t = e.split("&"), o = {}, i = 0, n = t.length; n > i; i++) {
        var r = t[i].split("=");
        if (2 == r.length) {
            var a = decodeURIComponent(r[0]),
                l = decodeURIComponent(r[1]);
            o[a] = l
        }
    }
    return o
}, QueryParams.prototype.setQuery = function (e) {
    var t = "";
    for (var o in e) if (e.hasOwnProperty(o)) {
        var i = e[o];
        void 0 != i && (t.length && (t += "&"), t += encodeURIComponent(o), t += "=", t += encodeURIComponent(e[o]))
    }
    window.location.search = t.length ? "#" + t : ""
}, QueryParams.prototype.getValue = function (e) {
    var t = this.getQuery();
    return t[e]
}, QueryParams.prototype.setValue = function (e, t) {
    var o = this.getQuery();
    o[e] = t, this.setQuery(o)
};
var ajax = function () {
    function e(e, t, o, i, n) {
        try {
            var r = new XMLHttpRequest;
            if (r.onreadystatechange = function () {
                4 == r.readyState && n(r.responseText, r.status)
            }, r.open(e, t, !0), i) for (var a in i) i.hasOwnProperty(a) && r.setRequestHeader(a, i[a]);
            r.send(o)
        } catch (l) {
            n(l, 0)
        }
    }
    function t(t, o, i) {
        e("GET", t, null, o, i)
    }
    function o(t, o, i, n) {
        e("POST", t, o, i, n)
    }
    return {
        fetch: e,
        get: t,
        post: o
    }
}(),
    FileRetriever = function (e) {
        e = e || {}, this.options = {
            maxSize: void 0 != e.maxSize ? e.maxSize : 1048576,
            html5: void 0 != e.html5 ? e.html5 : !0
        }, this.timeout = Number(e.timeout) || 3e4, this.headers = {
            Accept: "application/json"
        }, this.scriptUrl = e.scriptUrl || "/json/openUrl/", this.notify = e.notify || void 0, this.defaultFilename = "document.json", this.dom = {}
    };
FileRetriever.prototype._hide = function (e) {
    e.style.visibility = "hidden", e.style.position = "absolute", e.style.left = "-1000px", e.style.top = "-1000px", e.style.width = "0", e.style.height = "0"
}, FileRetriever.prototype.remove = function () {
    var e = this.dom;
    for (var t in e) if (e.hasOwnProperty(t)) {
        var o = e[t];
        o.parentNode && o.parentNode.removeChild(o)
    }
    this.dom = {}
}, FileRetriever.prototype._getFilename = function (e) {
    return e ? e.replace(/^.*[\\\/]/, "") : ""
}, FileRetriever.prototype.setUrl = function (e) {
    this.url = e
}, FileRetriever.prototype.getFilename = function () {
    return this.defaultFilename
}, FileRetriever.prototype.getUrl = function () {
    return this.url
}, FileRetriever.prototype.loadUrl = function (e, t) {
    this.setUrl(e);
    var o = void 0;
    this.notify && (o = this.notify.showNotification("loading url..."));
    var i = this,
        n = function (e, n) {
            t && (t(e, n), t = void 0), i.notify && o && (i.notify.removeMessage(o), o = void 0)
        },
        r = this.scriptUrl;
    ajax.get(e, i.headers, function (t, o) {
        if (200 == o) n(null, t);
        else {
            var a, l = r + "?url=" + encodeURIComponent(e);
            ajax.get(l, i.headers, function (t, o) {
                200 == o ? n(null, t) : 404 == o ? (console.log('Error: url "' + e + '" not found', o, t), a = new Error('Error: url "' + e + '" not found'), n(a, null)) : (console.log('Error: failed to load url "' + e + '"', o, t), a = new Error('Error: failed to load url "' + e + '"'), n(a, null))
            })
        }
    }), setTimeout(function () {
        n(new Error("Error loading url (time out)"))
    }, this.timeout)
}, FileRetriever.prototype.loadFile = function (e) {
    var t = void 0,
        o = this,
        i = function () {
            o.notify && !t && (t = o.notify.showNotification("loading file...")), setTimeout(function () {
                n(new Error("Error loading url (time out)"))
            }, o.timeout)
        },
        n = function (i, n) {
            e && (e(i, n), e = void 0), o.notify && t && (o.notify.removeMessage(t), t = void 0)
        },
        r = o.options.html5 && window.File && window.FileReader;
    if (r) this.prompt({
        title: "Open file",
        titleSubmit: "Open",
        description: "Select a file on your computer.",
        inputType: "file",
        inputName: "file",
        callback: function (e, t) {
            if (e) {
                if (r) {
                    var o = t.files[0],
                        a = new FileReader;
                    a.onload = function (e) {
                        var t = e.target.result;
                        n(null, t)
                    }, a.readAsText(o)
                }
                i()
            }
        }
    });
    else {
        var a = "fileretriever-upload-" + Math.round(1e15 * Math.random()),
            l = document.createElement("iframe");
        l.name = a, o._hide(l), l.onload = function () {
            var e = l.contentWindow.document.body.innerHTML;
            if (e) {
                var t = o.scriptUrl + "?id=" + e + "&filename=" + o.getFilename();
                ajax.get(t, o.headers, function (e, t) {
                    if (200 == t) n(null, e);
                    else {
                        var i = new Error("Error loading file " + o.getFilename());
                        n(i, null)
                    }
                    l.parentNode === document.body && document.body.removeChild(l)
                })
            }
        }, document.body.appendChild(l), this.prompt({
            title: "Open file",
            titleSubmit: "Open",
            description: "Select a file on your computer.",
            inputType: "file",
            inputName: "file",
            formAction: this.scriptUrl,
            formMethod: "POST",
            formTarget: a,
            callback: function (e) {
                e && i()
            }
        })
    }
}, FileRetriever.prototype.loadUrlDialog = function (e) {
    var t = this;
    this.prompt({
        title: "Open url",
        titleSubmit: "Open",
        description: "Enter a public url. Urls which need authentication or are located on an intranet cannot be loaded.",
        inputType: "text",
        inputName: "url",
        inputDefault: this.getUrl(),
        callback: function (o) {
            o ? t.loadUrl(o, e) : e()
        }
    })
}, FileRetriever.prototype.prompt = function (e) {
    var t = function () {
        h.parentNode && h.parentNode.removeChild(h), n.parentNode && n.parentNode.removeChild(n), util.removeEventListener(document, "keydown", i)
    },
        o = function () {
            t(), e.callback && e.callback(null)
        },
        i = util.addEventListener(document, "keydown", function (e) {
            var t = e.which;
            27 == t && (o(), e.preventDefault(), e.stopPropagation())
        }),
        n = document.createElement("div");
    n.className = "fileretriever-overlay", document.body.appendChild(n);
    var r = document.createElement("form");
    r.className = "fileretriever-form", r.target = e.formTarget || "", r.action = e.formAction || "", r.method = e.formMethod || "POST", r.enctype = "multipart/form-data", r.encoding = "multipart/form-data", r.onsubmit = function () {
        return s.value ? (setTimeout(function () {
            t()
        }, 0), e.callback && e.callback(s.value, s), void 0 != e.formAction && void 0 != e.formMethod) : (alert("Enter a " + e.inputName + " first..."), !1)
    };
    var a = document.createElement("div");
    if (a.className = "fileretriever-title", a.appendChild(document.createTextNode(e.title || "Dialog")), r.appendChild(a), e.description) {
        var l = document.createElement("div");
        l.className = "fileretriever-description", l.appendChild(document.createTextNode(e.description)), r.appendChild(l)
    }
    var s = document.createElement("input");
    s.className = "fileretriever-field", s.type = e.inputType || "text", s.name = e.inputName || "text", s.value = e.inputDefault || "";
    var d = document.createElement("div");
    d.className = "fileretriever-contents", d.appendChild(s), r.appendChild(d);
    var p = document.createElement("input");
    p.className = "fileretriever-cancel", p.type = "button", p.value = e.titleCancel || "Cancel", p.onclick = o;
    var c = document.createElement("input");
    c.className = "fileretriever-submit", c.type = "submit", c.value = e.titleSubmit || "Ok";
    var u = document.createElement("div");
    u.className = "fileretriever-buttons", u.appendChild(p), u.appendChild(c), r.appendChild(u);
    var m = document.createElement("div");
    m.className = "fileretriever-border", m.appendChild(r);
    var h = document.createElement("div");
    h.className = "fileretriever-background", h.appendChild(m), h.onclick = function (e) {
        var t = e.target;
        t == h && o()
    }, document.body.appendChild(h), s.focus(), s.select()
}, FileRetriever.prototype.saveFile = function (e, t) {
    var o = void 0;
    this.notify && (o = this.notify.showNotification("saving file..."));
    var i = this,
        n = function (e) {
            t && (t(e), t = void 0), i.notify && o && (i.notify.removeMessage(o), o = void 0)
        },
        r = document.createElement("a");
    this.options.html5 && void 0 != r.download && !util.isFirefox() ? (r.style.display = "none", r.href = "data:application/json;charset=utf-8," + encodeURIComponent(e), r.download = this.getFilename(), document.body.appendChild(r), r.click(), document.body.removeChild(r), n()) : e.length < this.options.maxSize ? ajax.post(i.scriptUrl, e, i.headers, function (e, t) {
        if (200 == t) {
            var o = document.createElement("iframe");
            o.src = i.scriptUrl + "?id=" + e + "&filename=" + i.getFilename(), i._hide(o), document.body.appendChild(o), n()
        } else n(new Error("Error saving file"))
    }) : n(new Error("Maximum allowed file size exceeded (" + this.options.maxSize + " bytes)")), setTimeout(function () {
        n(new Error("Error saving file (time out)"))
    }, this.timeout)
}, Notify.prototype.showNotification = function (e) {
    return this.showMessage({
        type: "notification",
        message: e,
        closeButton: !1
    })
}, Notify.prototype.showError = function (e) {
    return this.showMessage({
        type: "error",
        message: e.message ? "Error: " + e.message : e.toString(),
        closeButton: !0
    })
}, Notify.prototype.showMessage = function (e) {
    var t = this.dom.frame;
    if (!t) {
        var o = 500,
            i = 145,
            n = document.body.offsetWidth || window.innerWidth;
        t = document.createElement("div"), t.style.position = "absolute", t.style.left = (n - o) / 2 + "px", t.style.width = o + "px", t.style.top = i + "px", t.style.zIndex = "999", document.body.appendChild(t), this.dom.frame = t
    }
    var r = e.type || "notification",
        a = e.closeButton !== !1,
        l = document.createElement("div");
    l.className = r, l.type = r, l.closeable = a, l.style.position = "relative", t.appendChild(l);
    var s = document.createElement("table");
    s.style.width = "100%", l.appendChild(s);
    var d = document.createElement("tbody");
    s.appendChild(d);
    var p = document.createElement("tr");
    d.appendChild(p);
    var c = document.createElement("td");
    if (c.innerHTML = e.message || "", p.appendChild(c), a) {
        var u = document.createElement("td");
        u.style.textAlign = "right", u.style.verticalAlign = "top", p.appendChild(u);
        var m = document.createElement("button");
        m.innerHTML = "&times;", m.title = "Close message (ESC)", u.appendChild(m);
        var h = this;
        m.onclick = function () {
            h.removeMessage(l)
        }
    }
    return l
}, Notify.prototype.removeMessage = function (e) {
    var t = this.dom.frame;
    if (!e && t) {
        for (var o = t.firstChild; o && !o.closeable;) o = o.nextSibling;
        o && o.closeable && (e = o)
    }
    e && e.parentNode == t && e.parentNode.removeChild(e), t && 0 == t.childNodes.length && (t.parentNode.removeChild(t), delete this.dom.frame)
}, Notify.prototype.onKeyDown = function (e) {
    var t = e.which;
    27 == t && (this.removeMessage(), e.preventDefault(), e.stopPropagation())
}, Splitter.prototype.onMouseDown = function (e) {
    var t = this,
        o = e.which ? 1 == e.which : 1 == e.button;
    o && (util.addClassName(this.container, "active"), this.params.mousedown || (this.params.mousedown = !0, this.params.mousemove = util.addEventListener(document, "mousemove", function (e) {
        t.onMouseMove(e)
    }), this.params.mouseup = util.addEventListener(document, "mouseup", function (e) {
        t.onMouseUp(e)
    }), this.params.screenX = e.screenX, this.params.changed = !1, this.params.value = this.getValue()), e.preventDefault(), e.stopPropagation())
}, Splitter.prototype.onMouseMove = function (e) {
    if (void 0 != this.width) {
        var t = e.screenX - this.params.screenX,
            o = this.params.value + t / this.width;
        o = this.setValue(o), o != this.params.value && (this.params.changed = !0), this.onChange(o)
    }
    e.preventDefault(), e.stopPropagation()
}, Splitter.prototype.onMouseUp = function (e) {
    if (util.removeClassName(this.container, "active"), this.params.mousedown) {
        util.removeEventListener(document, "mousemove", this.params.mousemove), util.removeEventListener(document, "mouseup", this.params.mouseup), this.params.mousemove = void 0, this.params.mouseup = void 0, this.params.mousedown = !1;
        var t = this.getValue();
        this.params.changed || (0 == t && (t = this.setValue(.2), this.onChange(t)), 1 == t && (t = this.setValue(.8), this.onChange(t)))
    }
    e.preventDefault(), e.stopPropagation()
}, Splitter.prototype.setWidth = function (e) {
    this.width = e
}, Splitter.prototype.setValue = function (e) {
    e = Number(e), void 0 != this.width && this.width > this.snap && (e < this.snap / this.width && (e = 0), e > (this.width - this.snap) / this.width && (e = 1)), this.value = e;
    try {
        localStorage.splitterValue = e
    } catch (t) {
        console && console.log && console.log(t)
    }
    return e
}, Splitter.prototype.getValue = function () {
    var e = this.value;
    if (void 0 == e) try {
        void 0 != localStorage.splitterValue && (e = Number(localStorage.splitterValue), e = this.setValue(e))
    } catch (t) {
        console.log(t)
    }
    return void 0 == e && (e = this.setValue(.5)), e
};
var treeEditor = null,
    codeEditor = null,
    app = {};
app.CodeToTree = function () {
    try {
        treeEditor.set(codeEditor.get())
    } catch (e) {
        app.notify.showError(app.formatError(e))
    }
}, app.treeToCode = function () {
    try {
        codeEditor.set(treeEditor.get())
    } catch (e) {
        app.notify.showError(app.formatError(e))
    }
}, app.load = function (e) {
    try {
        app.notify = new Notify, app.retriever = new FileRetriever({
            scriptUrl: "/json/openUrl/",
            notify: app.notify
        });
         
        if (window.QueryParams) {
            var t = new QueryParams,
                o = t.getValue("url");
            o && (e = {}, app.openUrl(o))
        }
        app.lastChanged = void 0;
        var i = document.getElementById("codeEditor");
        codeEditor = new JSONEditor(i, {
            mode: "code",
            change: function () {
                app.lastChanged = codeEditor
            },
            error: function (e) {
                app.notify.showError(app.formatError(e))
            }
        }), codeEditor.set(e), i = document.getElementById("treeEditor"), treeEditor = new JSONEditor(i, {
            mode: "tree",
            change: function () {
                app.lastChanged = treeEditor
            },
            error: function (e) {
                app.notify.showError(app.formatError(e))
            }
        }), treeEditor.set(e), app.splitter = new Splitter({
            container: document.getElementById("drag"),
            change: function () {
                app.resize()
            }
        });
        var n = document.getElementById("toTree");
        n.onclick = function () {
            this.focus(), app.CodeToTree()
        };
        var r = document.getElementById("toCode");
        r.onclick = function () {
            this.focus(), app.treeToCode()
        }, util.addEventListener(window, "resize", app.resize);
        var a = document.getElementById("clear");
        a.onclick = app.clearFile;
        var l = document.getElementById("menuOpenFile");
        l.onclick = function (e) {
            app.openFile(), e.stopPropagation(), e.preventDefault()
        };
        var s = document.getElementById("menuOpenUrl");
        s.onclick = function (e) {
            app.openUrl(), e.stopPropagation(), e.preventDefault()
        };
        var d = document.getElementById("save");
        d.onclick = app.saveFile, codeEditor.focus(), document.body.spellcheck = !1
    } catch (p) {
        try {
            app.notify.showError(p)
        } catch (c) {
            console && console.log && console.log(p), alert(p)
        }
    }
}, app.openCallback = function (e, t) {
    if (e) app.notify.showError(e);
    else if (null != t) {
        codeEditor.setText(t);
        try {
            var o = util.parse(t);
            treeEditor.set(o)
        } catch (e) {
            treeEditor.set({}), app.notify.showError(app.formatError(e))
        }
    }
}, app.openFile = function () {
    app.retriever.loadFile(app.openCallback)
}, app.openUrl = function (e) {
    e ? app.retriever.loadUrl(e, app.openCallback) : app.retriever.loadUrlDialog(app.openCallback)
}, app.saveFile = function () {
    app.lastChanged == treeEditor && app.treeToCode(), app.lastChanged = void 0;
    var e = codeEditor.getText();
    app.retriever.saveFile(e, function (e) {
        e && app.notify.showError(e)
    })
}, app.formatError = function (e) {
    var t = '<pre class="error">' + e.toString() + "</pre>";
    return "undefined" != typeof jsonlint && (t += '<a class="error" href="http://zaach.github.com/jsonlint/" target="_blank">validated by jsonlint</a>'), t
}, app.clearFile = function () {
    var e = {};
    codeEditor.set(e), treeEditor.set(e)
}, app.resize = function () {
    var e = document.getElementById("menu"),
        t = document.getElementById("treeEditor"),
        o = document.getElementById("codeEditor"),
        i = document.getElementById("splitter"),
        n = document.getElementById("buttons"),
        r = document.getElementById("drag"),
        a = document.getElementById("ad"),
        l = 15,
        s = window.innerWidth || document.body.offsetWidth || document.documentElement.offsetWidth,
        d = a ? a.clientWidth : 0;
    if (d && (s -= d + l), app.splitter) {
        app.splitter.setWidth(s);
        var p = app.splitter.getValue(),
            c = p > 0,
            u = 1 > p,
            m = c && u;
        n.style.display = m ? "" : "none";
        var h, f = i.clientWidth;
        if (c) if (u) {
            h = s * p - f / 2;
            var v = 8 == util.getInternetExplorerVersion();
            r.innerHTML = v ? "|" : "&#8942;", r.title = "Drag left or right to change the width of the panels"
        } else h = s * p - f, r.innerHTML = "&lsaquo;", r.title = "Drag left to show the tree editor";
        else h = 0, r.innerHTML = "&rsaquo;", r.title = "Drag right to show the code editor";
        o.style.display = 0 == p ? "none" : "", o.style.width = 980 + "px", codeEditor.resize(), r.style.height = i.clientHeight - n.clientHeight - 2 * l - (m ? l : 0) + "px", r.style.lineHeight = r.style.height, t.style.display = 1 == p ? "none" : "", t.style.left = Math.round(h + f) + "px", t.style.width = Math.max(Math.round(s - h - f - 2), 0) + "px"
    }
    e && (e.style.right = d ? l + (d + l) + "px" : l + "px")
};