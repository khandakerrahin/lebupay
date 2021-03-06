/**
 * [js-md5]{@link https://github.com/emn178/js-md5}
 *
 * @namespace md5
 * @version 0.4.0
 * @author Chen, Yi-Cyuan [emn178@gmail.com]
 * @copyright Chen, Yi-Cyuan 2014-2016
 * @license MIT
 */
!function(t) {
	"use strict";
	function r(t) {
		if (t) c[0] = c[16] = c[1] = c[2] = c[3] = c[4] = c[5] = c[6] = c[7] = c[8] = c[9] = c[10] = c[11] = c[12] = c[13] = c[14] = c[15] = 0, this.blocks = c, this.buffer8 = i;
		else if (n) {
			var r = new ArrayBuffer(68);
			this.buffer8 = new Uint8Array(r), this.blocks = new Uint32Array(r)
		} else
			this.blocks = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
		this.h0 = this.h1 = this.h2 = this.h3 = this.start = this.bytes = 0, this.finalized = this.hashed = !1, this.first = !0
	}
	var e = "object" == typeof process && process.versions && process.versions.node;
	e && (t = global);
	var i,
		h = !t.JS_MD5_TEST && "object" == typeof module && module.exports,
		s = "function" == typeof define && define.amd,
		n = !t.JS_MD5_TEST && "undefined" != typeof ArrayBuffer,
		f = "0123456789abcdef".split(""),
		a = [ 128, 32768, 8388608, -2147483648 ],
		o = [ 0, 8, 16, 24 ],
		u = [ "hex", "array", "digest", "buffer", "arrayBuffer" ],
		c = [];
	if (n) {
		var p = new ArrayBuffer(68);
		i = new Uint8Array(p), c = new Uint32Array(p)
	}
	var y = function(t) {
			return function(e) {
				return new r(!0).update(e)[t]()
			}
		},
		d = function() {
			var t = y("hex");
			e && (t = l(t)), t.create = function() {
				return new r
			}, t.update = function(r) {
				return t.create().update(r)
			};
			for (var i = 0; i < u.length; ++i) {
				var h = u[i];
				t[h] = y(h)
			}
			return t
		},
		l = function(r) {
			var e,
				i;
			try {
				if (t.JS_MD5_TEST)
					throw "JS_MD5_TEST";
				e = require("crypto"), i = require("buffer").Buffer
			} catch (h) {
				return console.log(h), r
			}
			var s = function(t) {
				if ("string" == typeof t) return e.createHash("md5").update(t, "utf8").digest("hex");
				if (t.constructor == ArrayBuffer)
					t = new Uint8Array(t);
				else if (void 0 === t.length) return r(t);
				return e.createHash("md5").update(new i(t)).digest("hex")
			};
			return s
		};
	r.prototype.update = function(r) {
		if (!this.finalized) {
			var e = "string" != typeof r;
			e && r.constructor == t.ArrayBuffer && (r = new Uint8Array(r));
			for (var i, h, s = 0, f = r.length || 0, a = this.blocks, u = this.buffer8; f > s;) {
				if (this.hashed && (this.hashed = !1, a[0] = a[16], a[16] = a[1] = a[2] = a[3] = a[4] = a[5] = a[6] = a[7] = a[8] = a[9] = a[10] = a[11] = a[12] = a[13] = a[14] = a[15] = 0), e)
					if (n)
						for (h = this.start; f > s && 64 > h; ++s) u[h++] = r[s];
					else
						for (h = this.start; f > s && 64 > h; ++s) a[h >> 2] |= r[s] << o[3 & h++];
				else if (n)
					for (h = this.start; f > s && 64 > h; ++s) i = r.charCodeAt(s), 128 > i ? u[h++] = i : 2048 > i ? (u[h++] = 192 | i >> 6, u[h++] = 128 | 63 & i) : 55296 > i || i >= 57344 ? (u[h++] = 224 | i >> 12, u[h++] = 128 | i >> 6 & 63, u[h++] = 128 | 63 & i) : (i = 65536 + ((1023 & i) << 10 | 1023 & r.charCodeAt(++s)), u[h++] = 240 | i >> 18, u[h++] = 128 | i >> 12 & 63, u[h++] = 128 | i >> 6 & 63, u[h++] = 128 | 63 & i);
				else
					for (h = this.start; f > s && 64 > h; ++s) i = r.charCodeAt(s), 128 > i ? a[h >> 2] |= i << o[3 & h++] : 2048 > i ? (a[h >> 2] |= (192 | i >> 6) << o[3 & h++], a[h >> 2] |= (128 | 63 & i) << o[3 & h++]) : 55296 > i || i >= 57344 ? (a[h >> 2] |= (224 | i >> 12) << o[3 & h++], a[h >> 2] |= (128 | i >> 6 & 63) << o[3 & h++], a[h >> 2] |= (128 | 63 & i) << o[3 & h++]) : (i = 65536 + ((1023 & i) << 10 | 1023 & r.charCodeAt(++s)), a[h >> 2] |= (240 | i >> 18) << o[3 & h++], a[h >> 2] |= (128 | i >> 12 & 63) << o[3 & h++], a[h >> 2] |= (128 | i >> 6 & 63) << o[3 & h++], a[h >> 2] |= (128 | 63 & i) << o[3 & h++]);
				this.lastByteIndex = h, this.bytes += h - this.start, h >= 64 ? (this.start = h - 64, this.hash(), this.hashed = !0) : this.start = h
			}
			return this
		}
	}, r.prototype.finalize = function() {
		if (!this.finalized) {
			this.finalized = !0;
			var t = this.blocks,
				r = this.lastByteIndex;
			t[r >> 2] |= a[3 & r], r >= 56 && (this.hashed || this.hash(), t[0] = t[16], t[16] = t[1] = t[2] = t[3] = t[4] = t[5] = t[6] = t[7] = t[8] = t[9] = t[10] = t[11] = t[12] = t[13] = t[14] = t[15] = 0), t[14] = this.bytes << 3, this.hash()
		}
	}, r.prototype.hash = function() {
		var t,
			r,
			e,
			i,
			h,
			s,
			n = this.blocks;
		this.first ? (t = n[0] - 680876937, t = (t << 7 | t >>> 25) - 271733879 << 0, i = (-1732584194 ^ 2004318071 & t) + n[1] - 117830708, i = (i << 12 | i >>> 20) + t << 0, e = (-271733879 ^ i & (-271733879 ^ t)) + n[2] - 1126478375, e = (e << 17 | e >>> 15) + i << 0, r = (t ^ e & (i ^ t)) + n[3] - 1316259209, r = (r << 22 | r >>> 10) + e << 0) : (t = this.h0, r = this.h1, e = this.h2, i = this.h3, t += (i ^ r & (e ^ i)) + n[0] - 680876936, t = (t << 7 | t >>> 25) + r << 0, i += (e ^ t & (r ^ e)) + n[1] - 389564586, i = (i << 12 | i >>> 20) + t << 0, e += (r ^ i & (t ^ r)) + n[2] + 606105819, e = (e << 17 | e >>> 15) + i << 0, r += (t ^ e & (i ^ t)) + n[3] - 1044525330, r = (r << 22 | r >>> 10) + e << 0), t += (i ^ r & (e ^ i)) + n[4] - 176418897, t = (t << 7 | t >>> 25) + r << 0, i += (e ^ t & (r ^ e)) + n[5] + 1200080426, i = (i << 12 | i >>> 20) + t << 0, e += (r ^ i & (t ^ r)) + n[6] - 1473231341, e = (e << 17 | e >>> 15) + i << 0, r += (t ^ e & (i ^ t)) + n[7] - 45705983, r = (r << 22 | r >>> 10) + e << 0, t += (i ^ r & (e ^ i)) + n[8] + 1770035416, t = (t << 7 | t >>> 25) + r << 0, i += (e ^ t & (r ^ e)) + n[9] - 1958414417, i = (i << 12 | i >>> 20) + t << 0, e += (r ^ i & (t ^ r)) + n[10] - 42063, e = (e << 17 | e >>> 15) + i << 0, r += (t ^ e & (i ^ t)) + n[11] - 1990404162, r = (r << 22 | r >>> 10) + e << 0, t += (i ^ r & (e ^ i)) + n[12] + 1804603682, t = (t << 7 | t >>> 25) + r << 0, i += (e ^ t & (r ^ e)) + n[13] - 40341101, i = (i << 12 | i >>> 20) + t << 0, e += (r ^ i & (t ^ r)) + n[14] - 1502002290, e = (e << 17 | e >>> 15) + i << 0, r += (t ^ e & (i ^ t)) + n[15] + 1236535329, r = (r << 22 | r >>> 10) + e << 0, t += (e ^ i & (r ^ e)) + n[1] - 165796510, t = (t << 5 | t >>> 27) + r << 0, i += (r ^ e & (t ^ r)) + n[6] - 1069501632, i = (i << 9 | i >>> 23) + t << 0, e += (t ^ r & (i ^ t)) + n[11] + 643717713, e = (e << 14 | e >>> 18) + i << 0, r += (i ^ t & (e ^ i)) + n[0] - 373897302, r = (r << 20 | r >>> 12) + e << 0, t += (e ^ i & (r ^ e)) + n[5] - 701558691, t = (t << 5 | t >>> 27) + r << 0, i += (r ^ e & (t ^ r)) + n[10] + 38016083, i = (i << 9 | i >>> 23) + t << 0, e += (t ^ r & (i ^ t)) + n[15] - 660478335, e = (e << 14 | e >>> 18) + i << 0, r += (i ^ t & (e ^ i)) + n[4] - 405537848, r = (r << 20 | r >>> 12) + e << 0, t += (e ^ i & (r ^ e)) + n[9] + 568446438, t = (t << 5 | t >>> 27) + r << 0, i += (r ^ e & (t ^ r)) + n[14] - 1019803690, i = (i << 9 | i >>> 23) + t << 0, e += (t ^ r & (i ^ t)) + n[3] - 187363961, e = (e << 14 | e >>> 18) + i << 0, r += (i ^ t & (e ^ i)) + n[8] + 1163531501, r = (r << 20 | r >>> 12) + e << 0, t += (e ^ i & (r ^ e)) + n[13] - 1444681467, t = (t << 5 | t >>> 27) + r << 0, i += (r ^ e & (t ^ r)) + n[2] - 51403784, i = (i << 9 | i >>> 23) + t << 0, e += (t ^ r & (i ^ t)) + n[7] + 1735328473, e = (e << 14 | e >>> 18) + i << 0, r += (i ^ t & (e ^ i)) + n[12] - 1926607734, r = (r << 20 | r >>> 12) + e << 0, h = r ^ e, t += (h ^ i) + n[5] - 378558, t = (t << 4 | t >>> 28) + r << 0, i += (h ^ t) + n[8] - 2022574463, i = (i << 11 | i >>> 21) + t << 0, s = i ^ t, e += (s ^ r) + n[11] + 1839030562, e = (e << 16 | e >>> 16) + i << 0, r += (s ^ e) + n[14] - 35309556, r = (r << 23 | r >>> 9) + e << 0, h = r ^ e, t += (h ^ i) + n[1] - 1530992060, t = (t << 4 | t >>> 28) + r << 0, i += (h ^ t) + n[4] + 1272893353, i = (i << 11 | i >>> 21) + t << 0, s = i ^ t, e += (s ^ r) + n[7] - 155497632, e = (e << 16 | e >>> 16) + i << 0, r += (s ^ e) + n[10] - 1094730640, r = (r << 23 | r >>> 9) + e << 0, h = r ^ e, t += (h ^ i) + n[13] + 681279174, t = (t << 4 | t >>> 28) + r << 0, i += (h ^ t) + n[0] - 358537222, i = (i << 11 | i >>> 21) + t << 0, s = i ^ t, e += (s ^ r) + n[3] - 722521979, e = (e << 16 | e >>> 16) + i << 0, r += (s ^ e) + n[6] + 76029189, r = (r << 23 | r >>> 9) + e << 0, h = r ^ e, t += (h ^ i) + n[9] - 640364487, t = (t << 4 | t >>> 28) + r << 0, i += (h ^ t) + n[12] - 421815835, i = (i << 11 | i >>> 21) + t << 0, s = i ^ t, e += (s ^ r) + n[15] + 530742520, e = (e << 16 | e >>> 16) + i << 0, r += (s ^ e) + n[2] - 995338651, r = (r << 23 | r >>> 9) + e << 0, t += (e ^ (r | ~i)) + n[0] - 198630844, t = (t << 6 | t >>> 26) + r << 0, i += (r ^ (t | ~e)) + n[7] + 1126891415, i = (i << 10 | i >>> 22) + t << 0, e += (t ^ (i | ~r)) + n[14] - 1416354905, e = (e << 15 | e >>> 17) + i << 0, r += (i ^ (e | ~t)) + n[5] - 57434055, r = (r << 21 | r >>> 11) + e << 0, t += (e ^ (r | ~i)) + n[12] + 1700485571, t = (t << 6 | t >>> 26) + r << 0, i += (r ^ (t | ~e)) + n[3] - 1894986606, i = (i << 10 | i >>> 22) + t << 0, e += (t ^ (i | ~r)) + n[10] - 1051523, e = (e << 15 | e >>> 17) + i << 0, r += (i ^ (e | ~t)) + n[1] - 2054922799, r = (r << 21 | r >>> 11) + e << 0, t += (e ^ (r | ~i)) + n[8] + 1873313359, t = (t << 6 | t >>> 26) + r << 0, i += (r ^ (t | ~e)) + n[15] - 30611744, i = (i << 10 | i >>> 22) + t << 0, e += (t ^ (i | ~r)) + n[6] - 1560198380, e = (e << 15 | e >>> 17) + i << 0, r += (i ^ (e | ~t)) + n[13] + 1309151649, r = (r << 21 | r >>> 11) + e << 0, t += (e ^ (r | ~i)) + n[4] - 145523070, t = (t << 6 | t >>> 26) + r << 0, i += (r ^ (t | ~e)) + n[11] - 1120210379, i = (i << 10 | i >>> 22) + t << 0, e += (t ^ (i | ~r)) + n[2] + 718787259, e = (e << 15 | e >>> 17) + i << 0, r += (i ^ (e | ~t)) + n[9] - 343485551, r = (r << 21 | r >>> 11) + e << 0, this.first ? (this.h0 = t + 1732584193 << 0, this.h1 = r - 271733879 << 0, this.h2 = e - 1732584194 << 0, this.h3 = i + 271733878 << 0, this.first = !1) : (this.h0 = this.h0 + t << 0, this.h1 = this.h1 + r << 0, this.h2 = this.h2 + e << 0, this.h3 = this.h3 + i << 0)
	}, r.prototype.hex = function() {
		this.finalize();
		var t = this.h0,
			r = this.h1,
			e = this.h2,
			i = this.h3;
		return f[t >> 4 & 15] + f[15 & t] + f[t >> 12 & 15] + f[t >> 8 & 15] + f[t >> 20 & 15] + f[t >> 16 & 15] + f[t >> 28 & 15] + f[t >> 24 & 15] + f[r >> 4 & 15] + f[15 & r] + f[r >> 12 & 15] + f[r >> 8 & 15] + f[r >> 20 & 15] + f[r >> 16 & 15] + f[r >> 28 & 15] + f[r >> 24 & 15] + f[e >> 4 & 15] + f[15 & e] + f[e >> 12 & 15] + f[e >> 8 & 15] + f[e >> 20 & 15] + f[e >> 16 & 15] + f[e >> 28 & 15] + f[e >> 24 & 15] + f[i >> 4 & 15] + f[15 & i] + f[i >> 12 & 15] + f[i >> 8 & 15] + f[i >> 20 & 15] + f[i >> 16 & 15] + f[i >> 28 & 15] + f[i >> 24 & 15]
	}, r.prototype.toString = r.prototype.hex, r.prototype.digest = function() {
		this.finalize();
		var t = this.h0,
			r = this.h1,
			e = this.h2,
			i = this.h3;
		return [ 255 & t, t >> 8 & 255, t >> 16 & 255, t >> 24 & 255, 255 & r, r >> 8 & 255, r >> 16 & 255, r >> 24 & 255, 255 & e, e >> 8 & 255, e >> 16 & 255, e >> 24 & 255, 255 & i, i >> 8 & 255, i >> 16 & 255, i >> 24 & 255 ]
	}, r.prototype.array = r.prototype.digest, r.prototype.arrayBuffer = function() {
		this.finalize();
		var t = new ArrayBuffer(16),
			r = new Uint32Array(t);
		return r[0] = this.h0, r[1] = this.h1, r[2] = this.h2, r[3] = this.h3, t
	}, r.prototype.buffer = r.prototype.arrayBuffer;
	var v = d();
	h ? module.exports = v : (t.md5 = v, s && define(function() {
		return v
	}))
}(this);