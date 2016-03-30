'use strict';

angular.module('volunteercrowdApp')
    .controller('IncidenceDetailController', function ($scope, $rootScope, $stateParams, entity, Incidence, Administrator, Request) {
        $scope.incidence = entity;
        $scope.load = function (id) {
            Incidence.get({id: id}, function(result) {
                $scope.incidence = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:incidenceUpdate', function(event, result) {
            $scope.incidence = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
