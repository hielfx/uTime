'use strict';

angular.module('volunteercrowdApp').controller('PaymentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Payment', 'Request', 'AppUser',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Payment, Request, AppUser) {

        $scope.payment = entity;
        $scope.requests = Request.query({filter: 'payment-is-null'});
        $q.all([$scope.payment.$promise, $scope.requests.$promise]).then(function() {
            if (!$scope.payment.request || !$scope.payment.request.id) {
                return $q.reject();
            }
            return Request.get({id : $scope.payment.request.id}).$promise;
        }).then(function(request) {
            $scope.requests.push(request);
        });
        $scope.appusers = AppUser.query();
        $scope.load = function(id) {
            Payment.get({id : id}, function(result) {
                $scope.payment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:paymentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.payment.id != null) {
                Payment.update($scope.payment, onSaveSuccess, onSaveError);
            } else {
                Payment.save($scope.payment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForPaymentMoment = {};

        $scope.datePickerForPaymentMoment.status = {
            opened: false
        };

        $scope.datePickerForPaymentMomentOpen = function($event) {
            $scope.datePickerForPaymentMoment.status.opened = true;
        };
}]);
