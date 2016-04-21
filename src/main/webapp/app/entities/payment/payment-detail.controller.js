(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('PaymentDetailController', PaymentDetailController);

    PaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Payment', 'Request', 'AppUser', 'Assessment'];

    function PaymentDetailController($scope, $rootScope, $stateParams, entity, Payment, Request, AppUser, Assessment) {
        var vm = this;
        vm.payment = entity;
        vm.load = function (id) {
            Payment.get({id: id}, function(result) {
                vm.payment = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:paymentUpdate', function(event, result) {
            vm.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
