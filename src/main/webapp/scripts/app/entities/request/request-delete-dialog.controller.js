'use strict';

angular.module('volunteercrowdApp')
	.controller('RequestDeleteController', function($scope, $uibModalInstance, entity, Request) {

        $scope.request = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Request.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
