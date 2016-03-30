'use strict';

angular.module('volunteercrowdApp')
	.controller('DisponibilityDeleteController', function($scope, $uibModalInstance, entity, Disponibility) {

        $scope.disponibility = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Disponibility.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
