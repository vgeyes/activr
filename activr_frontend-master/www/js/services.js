angular.module('activr.services', [])

    .factory('SkillLevels', function() {
        var skillLevels = [{
            id: 0,
            name: 'beginner'
        }, {
            id: 1,
            name: 'intermediate'
        }, {
            id: 2,
            name: 'advanced'
        }, {
            id: 3,
            name: 'professional'
        }, {
            id: 4,
            name: 'really frickin good'
        }, {
            id: 5,
            name: 'you have no chance'
        }];

        return {
            all: function() {
                return skillLevels;
            },
            getById: function(aid) {
                for (var i = 0; i < skillLevels.length; i++) {
                    if (skillLevels[i].id === parseInt(aid)) {
                        return skillLevels[i];
                    }
                }
                return null;
            },
            getByName: function(aname) {
                for (var i = 0; i < skillLevels.length; i++) {
                    if (skillLevels[i].name === aname) {
                        return skillLevels[i];
                    }
                }
                return null;
            },
            getNameById: function(id) {
                for (var i = 0; i < skillLevels.length; i++) {
                    if (skillLevels[i].id === parseInt(id)) {
                        return skillLevels[i].name;
                    }
                }
                return null;
            }
        };
    })

    .factory('$localstorage', ['$window', function ($window) {
        return {
            set: function (key, value) {
                $window.localStorage[key] = value;
            },
            get: function (key, defaultValue) {
                return $window.localStorage[key] || defaultValue;
            },
            setObject: function (key, value) {
                $window.localStorage[key] = JSON.stringify(value);
            },
            getObject: function (key) {
                return JSON.parse($window.localStorage[key] || '{}');
            },
            delete: function(key) {
                $window.localStorage.removeItem(key);
            }
        }
    }])

    .factory('token', function($localstorage) {

        function storeTokenData(t){
            var expire_time = Math.floor(Date.now() / 1000) + parseInt(t.expires_in);
            //console.log(t);
            $localstorage.setObject('tokenData', t);
            $localstorage.set('tokenExpireTime', expire_time);
        }

        function getToken(){
            return $localstorage.getObject('tokenData').access_token;
        }

        function getExpireTime(){
            return $localstorage.get('tokenExpireTime');
        }

        function deleteTokenData(){
            $localstorage.delete('tokenData');
        }

        return {
            storeTokenData: storeTokenData,
            getToken: getToken,
            deleteTokenData: deleteTokenData,
            getExpireTime: getExpireTime
        }

    })

    .factory('api', function() {
        var apiURL = 'http://104.131.79.15:8080';

        return {
            url: apiURL
        }
    })

    .factory('Base64', function() {
        var keyStr = 'ABCDEFGHIJKLMNOP' +
            'QRSTUVWXYZabcdef' +
            'ghijklmnopqrstuv' +
            'wxyz0123456789+/' +
            '=';
        return {
            encode: function (input) {
                var output = "";
                var chr1, chr2, chr3 = "";
                var enc1, enc2, enc3, enc4 = "";
                var i = 0;

                do {
                    chr1 = input.charCodeAt(i++);
                    chr2 = input.charCodeAt(i++);
                    chr3 = input.charCodeAt(i++);

                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;

                    if (isNaN(chr2)) {
                        enc3 = enc4 = 64;
                    } else if (isNaN(chr3)) {
                        enc4 = 64;
                    }

                    output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                    chr1 = chr2 = chr3 = "";
                    enc1 = enc2 = enc3 = enc4 = "";
                } while (i < input.length);

                return output;
            },

            decode: function (input) {
                var output = "";
                var chr1, chr2, chr3 = "";
                var enc1, enc2, enc3, enc4 = "";
                var i = 0;

                // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
                var base64test = /[^A-Za-z0-9\+\/\=]/g;
                if (base64test.exec(input)) {
                    alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
                }
                input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

                do {
                    enc1 = keyStr.indexOf(input.charAt(i++));
                    enc2 = keyStr.indexOf(input.charAt(i++));
                    enc3 = keyStr.indexOf(input.charAt(i++));
                    enc4 = keyStr.indexOf(input.charAt(i++));

                    chr1 = (enc1 << 2) | (enc2 >> 4);
                    chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                    chr3 = ((enc3 & 3) << 6) | enc4;

                    output = output + String.fromCharCode(chr1);

                    if (enc3 != 64) {
                        output = output + String.fromCharCode(chr2);
                    }
                    if (enc4 != 64) {
                        output = output + String.fromCharCode(chr3);
                    }

                    chr1 = chr2 = chr3 = "";
                    enc1 = enc2 = enc3 = enc4 = "";

                } while (i < input.length);

                return output;
            }
        };
    });
