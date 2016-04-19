'use strict';

angular.module('volunteercrowdApp')
    .controller('NaturalPersonDetailController', function ($scope, $rootScope, $stateParams, entity, NaturalPerson, AppUser, Curriculum) {
        $scope.naturalPerson = entity;
        $scope.load = function (id) {
            NaturalPerson.get({id: id}, function(result) {
                $scope.naturalPerson = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:naturalPersonUpdate', function(event, result) {
            $scope.naturalPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
