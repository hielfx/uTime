'use strict';

angular.module('volunteercrowdApp').controller('AppUserDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AppUser', 'User',
        function($scope, $stateParams, $uibModalInstance, $q, entity, AppUser, User) {

        $scope.appUser = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            AppUser.get({id : id}, function(result) {
                $scope.appUser = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:appUserUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.appUser.id != null) {
                AppUser.update($scope.appUser, onSaveSuccess, onSaveError);
            } else {
                AppUser.save($scope.appUser, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
