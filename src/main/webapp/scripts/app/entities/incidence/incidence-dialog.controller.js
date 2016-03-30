'use strict';

angular.module('volunteercrowdApp').controller('IncidenceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Incidence', 'Administrator', 'Request',
        function($scope, $stateParams, $uibModalInstance, entity, Incidence, Administrator, Request) {

        $scope.incidence = entity;
        $scope.administrators = Administrator.query();
        $scope.requests = Request.query();
        $scope.load = function(id) {
            Incidence.get({id : id}, function(result) {
                $scope.incidence = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:incidenceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.incidence.id != null) {
                Incidence.update($scope.incidence, onSaveSuccess, onSaveError);
            } else {
                Incidence.save($scope.incidence, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationDate = {};

        $scope.datePickerForCreationDate.status = {
            opened: false
        };

        $scope.datePickerForCreationDateOpen = function($event) {
            $scope.datePickerForCreationDate.status.opened = true;
        };
}]);
