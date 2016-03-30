'use strict';

angular.module('volunteercrowdApp')
    .controller('DisponibilityDetailController', function ($scope, $rootScope, $stateParams, entity, Disponibility, Need) {
        $scope.disponibility = entity;
        $scope.load = function (id) {
            Disponibility.get({id: id}, function(result) {
                $scope.disponibility = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:disponibilityUpdate', function(event, result) {
            $scope.disponibility = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
