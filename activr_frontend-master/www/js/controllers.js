angular.module('activr.controllers', [])

    .controller('LoginCtrl', function ($scope, $http, $ionicLoading, $location, $ionicPopup, token, Base64, api, $localstorage) {

        // Form data for the login page
        $scope.$on('$ionicView.beforeEnter', function() {
            $scope.loginData = {};
            $scope.registerData = {};

            // Check if logged in
            if(token.getToken()){
                if(parseInt(token.getExpireTime()) < Math.floor(Date.now()/1000)){
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Your login has expired. Please log in again.'
                    });
                } else {
                    $location.path('/tab/dash');
                }
            }
        });

        $scope.loginBtnText = "Log In";

        $scope.changeLoginBtnText = function() {
            if($scope.loginData.noAccount){
                $scope.loginBtnText = "Create Account";
            } else {
                $scope.loginBtnText = "Log In";
            }
        };

        // Once we finish the functions below, un-comment those lines
        $scope.doLoginRegister = function() {
            // Save the username
            $localstorage.set('username', $scope.loginData.email);
            if($scope.loginData.noAccount){
                doRegister();
            } else {
                emailLogin();
            }
        };

        // Perform the login action when the user submits the login form
        var emailLogin = function() {
            $ionicLoading.show();
            $http.post(api.url + '/oauth/token',
                'password='+ $scope.loginData.password +
                '&username=' + $scope.loginData.email +
                '&grant_type=password' +
                '&scope=read write' +
                '&client_secret=123456' +
                '&client_id=clientapp'
            , {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Authorization': 'Basic ' + Base64.encode('clientapp' + ':' + '123456')
                }
            })
                .success(function (data, status, headers, config) {
                    // Verify login was successful, then probably redirect to dash
                    $ionicLoading.hide();
                    token.storeTokenData(data);

                    // Get list of activities from server
                    $http.get(api.url + '/activities', {
                        headers: {
                            'Authorization': 'Bearer ' + token.getToken()
                        }
                    })
                        .success(function(data){
                            //console.log(data);
                            if(data)
                                $localstorage.setObject('activities', data);
                        })
                        .error(function(data){
                            // Maybe show something here?
                        });

                    $location.path('/tab/dash');
                })
                .error(function (data, status, headers, config) {
                    // Probably show an error message
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Could not log you in.'
                    });
                });
        };

        // Perform the register action when the user submits the register form
        var doRegister = function() {
            $ionicLoading.show();
            $http.post(api.url + '/register', {
                username: $scope.loginData.email,
                password: $scope.loginData.password,
                password2: $scope.registerData.password2,
                f_name: $scope.registerData.first_name,
                l_name: $scope.registerData.last_name,
                phoneNumber: $scope.registerData.phoneNumber,
                gender: $scope.registerData.gender
            })
                .success(function (data, status, headers, config) {
                    // Do something with the data returned
                    $ionicLoading.hide();
                    $scope.loginData.password = '';
                    $scope.loginData.noAccount = false;
                    $scope.registerData = {};
                    $scope.loginBtnText = "Log In";
                    $ionicPopup.alert({
                        title: 'Success',
                        subTitle: 'Your account has been created. Please log in.'
                    });
                })
                .error(function (data, status, headers, config) {
                    // Probably show an error
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Could not create new profile.'
                    });
                });
        };

    })

    .controller('DashCtrl', function($scope, $ionicLoading, api, $ionicPopup, $http, $localstorage, token) {

        var username;

        $scope.$on('$ionicView.beforeEnter', function() {
            username = $localstorage.get('username');
            $scope.cards = [];
            $scope.potentialMatch = {};
            getPotentialMatch();
        });

        $scope.cardSwiped = function(direction) {
            console.log('swiped ' + direction);
            if(direction == 'left'){
                // DENY - post the same object
                postPotentialMatch(false);
            } else {
                // ACCEPT - set accepted to true, then post this object
                postPotentialMatch(true);
            }
        };

        $scope.cardDestroyed = function(index) {
            $scope.cards.splice(index, 1);
        };

        var notFoundCard =
        {
            "id": -1,
            "account2": {
                "interests": [],
                "id": 0,
                "username": null,
                "f_name": null,
                "l_name": null,
                "gender": null,
                "image": 'img/frown_margin.jpg'
            },
            "activity": null,
            "skill": null,
            "accepted": false
        };

        function getPotentialMatch() {
            console.log('Getting potential match...');
            $ionicLoading.show();
            $http.get(api.url + '/' + username + '/potentialMatch', {
                headers: { 'Authorization': 'Bearer ' + token.getToken() }
            })
                .success(function (data) {
                    //console.log(data);
                    $ionicLoading.hide();
                    if(data) {
                        $scope.potentialMatch = data;
                        // Add image data
                        var potentialMatch = data;
                        potentialMatch.account2.image = api.url + '/images/profilePics/' +
                                                        potentialMatch.account2.username + '/profilePicture.jpg';
                                                        //'img/default_profile_pic_wide.jpg';
                        // Determine their skill level at the matched activity
                        for(var i=0; i<potentialMatch.account2.interests.length; i++){
                            if(potentialMatch.account2.interests[i].activity == potentialMatch.activity){
                                potentialMatch.skill = potentialMatch.account2.interests[i].skill;
                                break;
                            }
                        }
                        if(!potentialMatch.account2.f_name){
                            potentialMatch.account2.f_name = 'No name provided';
                        }
                        // Add to cards stack
                        $scope.cards.push(potentialMatch);
                        //console.log(potentialMatch);
                    } else {
                        $scope.potentialMatch = notFoundCard;
                        $scope.cards.push(angular.copy(notFoundCard));
                    }
                })
                .error(function (data) {
                    $ionicLoading.hide();
                    if ('message' in data && data.message == 'No entity found for query') {
                        $scope.potentialMatch = notFoundCard;
                        $scope.cards.push(angular.copy(notFoundCard));
                    } else {
                        $scope.potentialMatch = notFoundCard;
                        $scope.cards.push(angular.copy(notFoundCard));
                        // Maybe show something here?
                        //$ionicPopup.alert({
                        //    title: 'Error',
                        //    subTitle: 'Could not retrieve a potential match.'
                        //});
                    }
                });
        }

        function postPotentialMatch(accepted){
            if($scope.potentialMatch.id < 0){
                getPotentialMatch();
                return;
            }
            errorWord = 'deny';
            successWord = 'denied'
            if(accepted){
                $scope.potentialMatch.accepted = true;
                errorWord = 'accept';
                successWord = 'accepted'
            }
            $http.post(api.url + '/' + username + '/potentialMatch', $scope.potentialMatch,{
                headers: { 'Authorization': 'Bearer ' + token.getToken() }
            })
                .success(function(data){
                    console.log('Request ' + successWord + ' for user ' + $scope.potentialMatch.account2.username +
                        ' and activity ' + $scope.potentialMatch.activity + '.');
                    window.plugins.toast.showShortBottom('Match ' + successWord + '.');
                })
                .error(function(data){
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Could not ' + errorWord + ' this match.'
                    });
                })
                .then(function(){
                    getPotentialMatch();
                });
        }



    })

    .controller('MatchesCtrl', function($scope, $http, $ionicLoading, $ionicPopup, api, $localstorage, token) {

        var username;

        $scope.$on('$ionicView.beforeEnter', function() {
            username = $localstorage.get('username');
            $scope.matches = [];
            getMatches();
        });

        function getMatches(){
            $ionicLoading.show();
            $http.get(api.url + '/' + username + '/matches', {
                headers: { 'Authorization': 'Bearer ' + token.getToken() }
            })
                .success(function (data) {
                    //console.log(data);
                    var matches = data;
                    // Determine their skill level at the matched activity
                    for(var i=0; i<matches.length; i++) {
                        for (var j = 0; j < matches[i].account2.interests.length; j++) {
                            if (matches[i].account2.interests[j].activity == matches[i].activity) {
                                matches[i].skill = matches[i].account2.interests[j].skill;
                                break;
                            }
                        }
                        if(!matches[i].account2.f_name){
                            matches[i].account2.f_name = 'No name provided'
                        }
                        if(!('phoneNumber' in matches[i].account2) || !matches[i].account2.phoneNumber){
                            matches[i].account2.phoneNumber = 'No phone number provided'
                        }
                        matches[i].account2.image = api.url + '/images/profilePics/' + matches[i].account2.username +
                                                    '/profilePicture.jpg';
                        // 'img/default_profile_pic.jpg';
                    }
                    $scope.matches = matches;
                    $ionicLoading.hide();
                })
                .error(function (data) {
                    $ionicLoading.hide();
                    // Maybe show something here?
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Could not retrieve your matches.'
                    });
                });
        }

    })

    .controller('AccountCtrl', function($scope, $location, token, $localstorage, $http, api, $ionicPopup, $ionicLoading) {

        var profileHasChanged;
        var username;

        function copyInterests(){
            // Loop through activities list and add returned activities to it
            for(var i= 0; i<$scope.activities.length; i++){
                var skill = 1;
                var primary = false;
                var isInterest = false;

                if('interests' in $scope.account) {
                    for (var j = 0; j < $scope.account.interests.length; j++) {
                        if ($scope.account.interests[j].activity == $scope.activities[i]) {
                            skill = $scope.account.interests[j].skill;
                            primary = $scope.account.interests[j].primary;
                            isInterest = true;
                            if(primary){
                                $scope.mainActivity.activity = $scope.activities[i];
                                $scope.mainActivity.skill = $scope.account.interests[j].skill;
                            }
                            break;
                        }
                    }
                }

                $scope.account2.interests[i] = {
                    isInterest: isInterest,
                    activity: $scope.activities[i],
                    skill: skill,
                    isPrimary: primary
                };
            }
            $scope.account2.f_name = $scope.account.f_name;
            $scope.account2.l_name = $scope.account.l_name;
            $scope.account2.gender = $scope.account.gender;
            $scope.account2.username = $scope.account.username;
            $scope.account2.phoneNumber = $scope.account.phoneNumber;
            $scope.account2.image = api.url + '/images/profilePics/' + username + '/profilePicture.jpg';

            console.log($scope.account);
            console.log('account2 object');
            console.log($scope.account2);
        }

        function loadInterests() {
            $ionicLoading.show();
            // Load the user's account from the server
            $http.get(api.url + '/' + username + '/account', {
                headers: { 'Authorization': 'Bearer ' + token.getToken() }
            })
                .success(function (data) {
                    //console.log(data);
                    $ionicLoading.hide();
                    if (data) {
                        $localstorage.setObject('account', data);
                        $scope.account = data;
                        copyInterests();
                    }
                })
                .error(function (data) {
                    $ionicLoading.hide();
                    // Maybe show something here?
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Could not retrieve your account.'
                    })
                        .then(function(){
                            profileHasChanged = false;
                            $location.path('/tab/dash');
                        });
                });
        }

        var updateAccount = function() {

            if(!profileHasChanged) return;

            var newAccount = $scope.account2;

            for(var i=0; i<newAccount.interests.length; i++){
                newAccount.interests[i].isPrimary = false;
                // Set main interest
                if(newAccount.interests[i].activity == $scope.mainActivity.activity){
                    newAccount.interests[i].skill = $scope.mainActivity.skill;
                    newAccount.interests[i].isPrimary = true;
                    newAccount.interests[i].isInterest = true;
                }
            }

            // Remove all elements that are not checked
            for(var i=newAccount.interests.length-1; i>=0; i--) {
                if(!newAccount.interests[i].isInterest) {
                    newAccount.interests.splice(i, 1);
                } else {
                    delete newAccount.interests[i].isInterest;
                }
            }

            $http.post(api.url + '/' + username + '/account', newAccount,{
                headers: { 'Authorization': 'Bearer ' + token.getToken() }
            })
                .success(function(data){
                    $localstorage.setObject('account', data);
                })
                .error(function(data){
                    $ionicPopup.alert({
                        title: 'Error',
                        subTitle: 'Could not update your account.'
                    });
                });
            console.log(newAccount);
        };

        $scope.$on('$ionicView.beforeLeave', function() {
            updateAccount();
        });

        $scope.$on('$ionicView.beforeEnter', function() {
            username = $localstorage.get('username');
            $scope.activities = $localstorage.getObject('activities');
            $scope.mainActivity = {};
            $scope.account = {};
            $scope.account.interests = [];
            $scope.account2 = {};
            $scope.account2.interests = [];
            profileHasChanged = true;
            loadInterests();
        });

        $scope.changePic = function() {
            navigator.camera.getPicture(function(imageURI) {
                $scope.account2.image = imageURI;
                // Now upload the image
                uploadImage(imageURI);
            }, function(err) {
                // Ruh-roh, something bad happened
            }, {
                correctOrientation: true,
                targetWidth: 800,
                targetHeight: 800,
                quality: 50,
                allowEdit: true
            });
        };

        function uploadImage(uri){
            $ionicLoading.show();

            var win = function (r) {
                console.log("Code = " + r.responseCode);
                console.log("Response = " + r.response);
                console.log("Sent = " + r.bytesSent);
                $ionicLoading.hide();
                window.plugins.toast.showShortBottom('Updated profile picture.')
            };

            var fail = function (error) {
                //alert("An error has occurred: Code = " + error.code);
                console.log("upload error source " + error.source);
                console.log("upload error target " + error.target);
                $ionicLoading.hide();
                window.plugins.toast.showShortBottom('Error updating profile picture.')
            };

            var options = new FileUploadOptions();
            //options.fileKey = "file";
            options.fileName = uri.substr(uri.lastIndexOf('/') + 1);
            options.mimeType = "image/jpg";

            var params = {};
            //params.value1 = "test";
            //params.value2 = "param";
            options.params = params;
            var headers={ 'Authorization': 'Bearer ' + token.getToken() };
            options.headers = headers;

            var ft = new FileTransfer();
            var uploadURL = api.url + '/' + username + '/profilePicture';
            ft.upload(uri, encodeURI(uploadURL), win, fail, options);

        }

        $scope.logout = function() {
            updateAccount();
            profileHasChanged = false;
            // Clear login data and session, then redirect to login page
            window.localStorage.clear();
            $location.path('/start/login');
        }
    });
