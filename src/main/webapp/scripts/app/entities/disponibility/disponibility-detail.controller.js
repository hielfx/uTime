'use strict';

angular.module('volunteercrowdApp')
    .controller('AvailabilityDetailController', function ($scope, $rootScope, $stateParams, entity, Availability, Need) {
        $scope.availability = entity;
        $scope.load = function (id) {
            Availability.get({id: id}, function (result) {
                $scope.availability = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:availabilityUpdate', function (event, result) {
            $scope.availability = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
