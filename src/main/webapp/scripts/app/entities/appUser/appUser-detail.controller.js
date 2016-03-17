'use strict';

angular.module('volunteercrowdApp')
    .controller('AppUserDetailController', function ($scope, $rootScope, $stateParams, entity, AppUser, User) {
        $scope.appUser = entity;
        $scope.load = function (id) {
            AppUser.get({id: id}, function(result) {
                $scope.appUser = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:appUserUpdate', function(event, result) {
            $scope.appUser = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
