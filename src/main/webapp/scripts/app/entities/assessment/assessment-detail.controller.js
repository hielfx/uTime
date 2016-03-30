'use strict';

angular.module('volunteercrowdApp')
    .controller('AssessmentDetailController', function ($scope, $rootScope, $stateParams, entity, Assessment, AppUser) {
        $scope.assessment = entity;
        $scope.load = function (id) {
            Assessment.get({id: id}, function(result) {
                $scope.assessment = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:assessmentUpdate', function(event, result) {
            $scope.assessment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
