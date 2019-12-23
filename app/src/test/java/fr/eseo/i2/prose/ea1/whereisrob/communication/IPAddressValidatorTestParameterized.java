/**
 * @file IPAddressValidatorTestPAarameterized.java
 *
 * @brief La classe de test qui permet de vérifier le format d'une adresse IPv4.
 *
 * @author mkyong, Timothée GIRARD
 *
 * @copyright https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 *
 * 2019 ProseA1
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.eseo.i2.prose.ea1.whereisrob.communication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;
import fr.eseo.i2.prose.ea1.whereisrob.communication.IPAddressValidator;
import static org.junit.Assert.assertEquals;

/**
 * La classe de test qui permet de vérifier le format d'une adresse IPv4.
 */
@RunWith(Parameterized.class)
public class IPAddressValidatorTestParameterized {

    private String ipAddress;
    private boolean response;

    private IPAddressValidator ipValidator = new IPAddressValidator();

    public IPAddressValidatorTestParameterized(String ipAddress, boolean response){
        this.ipAddress = ipAddress;
        this.response = response;
    }

    @Parameters(name = "dt[{index}] : {0}, {1}")
    public static Collection<Object[]> dt() {

        Object[][] data = new Object[][] {

                // Valid ip address
                {"1.1.1.1", true},
                {"255.255.255.255", true},
                {"192.168.1.1", true},
                {"10.10.1.1", true},
                {"132.254.111.10", true},
                {"26.10.2.10", true},
                {"127.0.0.1", true},

                {"192.168.1.98", true},
                {"172.23.3.3", true},

                // Invalid ip address
                {"10.10.10", false},
                {"10.10", false},
                {"10", false},
                {"a.a.a.a", false},
                {"10.0.0.a", false},
                {"10.10.10.256", false},
                {"222.222.2.999", false},
                {"999.10.10.20", false},
                {"2222.22.22.22", false},
                {"22.2222.22.2", false},

                {"ersdv", false},
                {"y,utnhgb", false},
                {"bf zsdfc", false},
                {"", false},

        };
        return Arrays.asList(data);
    }

    @Test
    public void IPAddressValidator() {
        assertEquals(response, ipValidator.validate(ipAddress));
    }

}   // End of class
