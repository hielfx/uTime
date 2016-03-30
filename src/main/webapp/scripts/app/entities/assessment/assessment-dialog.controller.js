'use strict';

angular.module('volunteercrowdApp').controller('AssessmentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Assessment', 'AppUser',
        function($scope, $stateParams, $uibModalInstance, entity, Assessment, AppUser) {

        $scope.assessment = entity;
        $scope.appusers = AppUser.query();
        $scope.load = function(id) {
            Assessment.get({id : id}, function(result) {
                $scope.assessment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:assessmentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.assessment.id != null) {
                Assessment.update($scope.assessment, onSaveSuccess, onSaveError);
            } else {
                Assessment.save($scope.assessment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationMoment = {};

        $scope.datePickerForCreationMoment.status = {
            opened: false
        };

        $scope.datePickerForCreationMomentOpen = function($event) {
            $scope.datePickerForCreationMoment.status.opened = true;
        };
}]);
