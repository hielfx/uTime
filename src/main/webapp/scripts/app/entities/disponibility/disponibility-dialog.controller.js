'use strict';

angular.module('volunteercrowdApp').controller('AvailabilityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Availability', 'Need',
        function ($scope, $stateParams, $uibModalInstance, entity, Availability, Need) {

            $scope.availability = entity;
        $scope.needs = Need.query();
        $scope.load = function(id) {
            Availability.get({id: id}, function (result) {
                $scope.availability = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:availabilityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.availability.id != null) {
                Availability.update($scope.availability, onSaveSuccess, onSaveError);
            } else {
                Availability.save($scope.availability, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartMoment = {};

        $scope.datePickerForStartMoment.status = {
            opened: false
        };

        $scope.datePickerForStartMomentOpen = function($event) {
            $scope.datePickerForStartMoment.status.opened = true;
        };
        $scope.datePickerForEndMoment = {};

        $scope.datePickerForEndMoment.status = {
            opened: false
        };

        $scope.datePickerForEndMomentOpen = function($event) {
            $scope.datePickerForEndMoment.status.opened = true;
        };
}]);
