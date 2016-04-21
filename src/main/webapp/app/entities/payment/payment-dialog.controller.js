(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('PaymentDialogController', PaymentDialogController);

    PaymentDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Payment', 'Request', 'AppUser', 'Assessment'];

    function PaymentDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Payment, Request, AppUser, Assessment) {
        var vm = this;
        vm.payment = entity;
        vm.requests = Request.query({filter: 'payment-is-null'});
        $q.all([vm.payment.$promise, vm.requests.$promise]).then(function() {
            if (!vm.payment.request || !vm.payment.request.id) {
                return $q.reject();
            }
            return Request.get({id : vm.payment.request.id}).$promise;
        }).then(function(request) {
            vm.requests.push(request);
        });
        vm.appusers = AppUser.query();
        vm.assessments = Assessment.query();
        vm.load = function(id) {
            Payment.get({id : id}, function(result) {
                vm.payment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:paymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.payment.id !== null) {
                Payment.update(vm.payment, onSaveSuccess, onSaveError);
            } else {
                Payment.save(vm.payment, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.paymentMoment = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
