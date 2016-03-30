'use strict';

angular.module('volunteercrowdApp')
    .controller('PaymentDetailController', function ($scope, $rootScope, $stateParams, entity, Payment, Request, AppUser) {
        $scope.payment = entity;
        $scope.load = function (id) {
            Payment.get({id: id}, function(result) {
                $scope.payment = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:paymentUpdate', function(event, result) {
            $scope.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
