'use strict';

describe('Controller Tests', function() {

    describe('Request Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRequest, MockAppUser, MockNeed, MockPayment, MockIncidence, MockRequestStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRequest = jasmine.createSpy('MockRequest');
            MockAppUser = jasmine.createSpy('MockAppUser');
            MockNeed = jasmine.createSpy('MockNeed');
            MockPayment = jasmine.createSpy('MockPayment');
            MockIncidence = jasmine.createSpy('MockIncidence');
            MockRequestStatus = jasmine.createSpy('MockRequestStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Request': MockRequest,
                'AppUser': MockAppUser,
                'Need': MockNeed,
                'Payment': MockPayment,
                'Incidence': MockIncidence,
                'RequestStatus': MockRequestStatus
            };
            createController = function() {
                $injector.get('$controller')("RequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:requestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
