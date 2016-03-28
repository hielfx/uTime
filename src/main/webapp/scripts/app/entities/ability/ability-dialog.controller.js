'use strict';

angular.module('volunteercrowdApp').controller('AbilityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ability', 'Tag', 'AppUser',
        function($scope, $stateParams, $uibModalInstance, entity, Ability, Tag, AppUser) {

        $scope.ability = entity;
        $scope.tags = Tag.query();
        $scope.appusers = AppUser.query();
        $scope.load = function(id) {
            Ability.get({id : id}, function(result) {
                $scope.ability = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:abilityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.ability.id != null) {
                Ability.update($scope.ability, onSaveSuccess, onSaveError);
            } else {
                Ability.save($scope.ability, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
