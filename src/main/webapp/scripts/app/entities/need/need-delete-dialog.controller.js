'use strict';

angular.module('volunteercrowdApp')
	.controller('NeedDeleteController', function($scope, $uibModalInstance, entity, Need) {

        $scope.need = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Need.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
