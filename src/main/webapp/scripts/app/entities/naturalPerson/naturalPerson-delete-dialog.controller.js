'use strict';

angular.module('volunteercrowdApp')
	.controller('NaturalPersonDeleteController', function($scope, $uibModalInstance, entity, NaturalPerson) {

        $scope.naturalPerson = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            NaturalPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
