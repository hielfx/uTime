'use strict';

angular.module('volunteercrowdApp').controller('NeedDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Need', 'AppUser', 'NeededAbility', 'Availability',
        function ($scope, $stateParams, $uibModalInstance, entity, Need, AppUser, NeededAbility, Availability) {

        $scope.need = entity;
        $scope.appusers = AppUser.query();
        $scope.neededabilitys = NeededAbility.query();
            $scope.availabilitys = Availability.query();
        $scope.load = function(id) {
            Need.get({id : id}, function(result) {
                $scope.need = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:needUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.need.id != null) {
                Need.update($scope.need, onSaveSuccess, onSaveError);
            } else {
                Need.save($scope.need, onSaveSuccess, onSaveError);
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
        $scope.datePickerForModificationDate = {};

        $scope.datePickerForModificationDate.status = {
            opened: false
        };

        $scope.datePickerForModificationDateOpen = function($event) {
            $scope.datePickerForModificationDate.status.opened = true;
        };
}]);
