'use strict';

angular.module('volunteercrowdApp').controller('RequestDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Request', 'AppUser',
        function($scope, $stateParams, $uibModalInstance, entity, Request, AppUser) {

        $scope.request = entity;
        $scope.appusers = AppUser.query();
        $scope.load = function(id) {
            Request.get({id : id}, function(result) {
                $scope.request = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:requestUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.request.id != null) {
                Request.update($scope.request, onSaveSuccess, onSaveError);
            } else {
                Request.save($scope.request, onSaveSuccess, onSaveError);
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
        $scope.datePickerForFinishDate = {};

        $scope.datePickerForFinishDate.status = {
            opened: false
        };

        $scope.datePickerForFinishDateOpen = function($event) {
            $scope.datePickerForFinishDate.status.opened = true;
        };
}]);
