'use strict';

angular.module('volunteercrowdApp')
    .controller('RequestDetailController', function ($scope, $rootScope, $stateParams, entity, Request, AppUser) {
        $scope.request = entity;
        $scope.load = function (id) {
            Request.get({id: id}, function(result) {
                $scope.request = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:requestUpdate', function(event, result) {
            $scope.request = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
