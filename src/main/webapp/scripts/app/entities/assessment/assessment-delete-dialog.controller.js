'use strict';

angular.module('volunteercrowdApp')
	.controller('AssessmentDeleteController', function($scope, $uibModalInstance, entity, Assessment) {

        $scope.assessment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Assessment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
