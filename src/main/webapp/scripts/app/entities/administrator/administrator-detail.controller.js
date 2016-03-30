'use strict';

angular.module('volunteercrowdApp')
    .controller('AdministratorDetailController', function ($scope, $rootScope, $stateParams, entity, Administrator) {
        $scope.administrator = entity;
        $scope.load = function (id) {
            Administrator.get({id: id}, function(result) {
                $scope.administrator = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:administratorUpdate', function(event, result) {
            $scope.administrator = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
