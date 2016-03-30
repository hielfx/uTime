'use strict';

describe('Controller Tests', function() {

    describe('Payment Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPayment, MockRequest, MockAppUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPayment = jasmine.createSpy('MockPayment');
            MockRequest = jasmine.createSpy('MockRequest');
            MockAppUser = jasmine.createSpy('MockAppUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Payment': MockPayment,
                'Request': MockRequest,
                'AppUser': MockAppUser
            };
            createController = function() {
                $injector.get('$controller')("PaymentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:paymentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
