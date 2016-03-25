'use strict';

angular.module('volunteercrowdApp')
	.controller('CurriculumDeleteController', function($scope, $uibModalInstance, entity, Curriculum) {

        $scope.curriculum = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Curriculum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
