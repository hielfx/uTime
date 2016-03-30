'use strict';

angular.module('volunteercrowdApp').controller('DisponibilityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Disponibility', 'Need',
        function($scope, $stateParams, $uibModalInstance, entity, Disponibility, Need) {

        $scope.disponibility = entity;
        $scope.needs = Need.query();
        $scope.load = function(id) {
            Disponibility.get({id : id}, function(result) {
                $scope.disponibility = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:disponibilityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.disponibility.id != null) {
                Disponibility.update($scope.disponibility, onSaveSuccess, onSaveError);
            } else {
                Disponibility.save($scope.disponibility, onSaveSuccess, onSaveError);
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
