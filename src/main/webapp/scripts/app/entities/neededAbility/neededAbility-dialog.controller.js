'use strict';

angular.module('volunteercrowdApp').controller('NeededAbilityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'NeededAbility', 'Need',
        function($scope, $stateParams, $uibModalInstance, entity, NeededAbility, Need) {

        $scope.neededAbility = entity;
        $scope.needs = Need.query();
        $scope.load = function(id) {
            NeededAbility.get({id : id}, function(result) {
                $scope.neededAbility = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:neededAbilityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.neededAbility.id != null) {
                NeededAbility.update($scope.neededAbility, onSaveSuccess, onSaveError);
            } else {
                NeededAbility.save($scope.neededAbility, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
