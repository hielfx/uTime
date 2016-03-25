'use strict';

angular.module('volunteercrowdApp').controller('CurriculumDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Curriculum', 'NaturalPerson',
        function($scope, $stateParams, $uibModalInstance, entity, Curriculum, NaturalPerson) {

        $scope.curriculum = entity;
        $scope.naturalpersons = NaturalPerson.query();
        $scope.load = function(id) {
            Curriculum.get({id : id}, function(result) {
                $scope.curriculum = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:curriculumUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.curriculum.id != null) {
                Curriculum.update($scope.curriculum, onSaveSuccess, onSaveError);
            } else {
                Curriculum.save($scope.curriculum, onSaveSuccess, onSaveError);
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
