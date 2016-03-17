'use strict';

angular.module('volunteercrowdApp')
	.controller('AppUserDeleteController', function($scope, $uibModalInstance, entity, AppUser) {

        $scope.appUser = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AppUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
