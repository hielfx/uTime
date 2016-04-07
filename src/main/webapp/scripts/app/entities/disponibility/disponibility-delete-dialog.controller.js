'use strict';

angular.module('volunteercrowdApp')
    .controller('AvailabilityDeleteController', function ($scope, $uibModalInstance, entity, Availability) {

        $scope.availability = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Availability.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
