'use strict';

angular.module('volunteercrowdApp')
    .controller('CurriculumDetailController', function ($scope, $rootScope, $stateParams, entity, Curriculum, NaturalPerson) {
        $scope.curriculum = entity;
        $scope.load = function (id) {
            Curriculum.get({id: id}, function(result) {
                $scope.curriculum = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:curriculumUpdate', function(event, result) {
            $scope.curriculum = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
